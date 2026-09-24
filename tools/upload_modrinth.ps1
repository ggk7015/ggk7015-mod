param(
    [string]$Token = $env:MODRINTH_TOKEN,
    [string]$GameVersion = $env:GAME_VERSION,
    [string]$JarPath = "build\libs\ggk7015-mod-1.0.0.jar"
)

# 發布 ggk7015-mod 到 Modrinth（Hackcraft 發放 Java 帳號的門檻）
# 用法：
#   .\tools\upload_modrinth.ps1
# 或指定：
#   $env:MODRINTH_TOKEN = "mrpat_你的金鑰"; .\tools\upload_modrinth.ps1
# 需要的金鑰請到 modrinth.com → Settings → API tokens 建立（讀寫權限）。

$ErrorActionPreference = "Stop"
if ([string]::IsNullOrWhiteSpace($Token)) {
    throw "缺少 MODRINTH_TOKEN。請先在 https://modrinth.com/settings/tokens 建立 token 後重設 \$env:MODRINTH_TOKEN"
}
if (-not (Test-Path $JarPath)) {
    throw "找不到 jar：$JarPath（先跑 .\gradlew.bat build）"
}

$api = "https://api.modrinth.com/v2"
$headers = @{ Authorization = $Token; "User-Agent" = "ggk7015-mod/1.0.0 (Hackcraft YSWS)" }

if ([string]::IsNullOrWhiteSpace($GameVersion)) {
    # 26.2 nightly 的遊戲版本字串需以官方 tag 為準
    $GameVersion = (Invoke-RestMethod "$api/tag/game_version" -Headers $headers |
        Where-Object { $_.version -match "26\.2" } | Select-Object -First 1).version
    if (-not $GameVersion) { throw "抓不到 26.2 遊戲版本 tag；請查 $api/tag/game_version 並設 \$env:GAME_VERSION" }
}
Write-Host "遊戲版本：$GameVersion"

# 1) 查使用者（拿 team/user id）
$me = Invoke-RestMethod "$api/user" -Headers $headers

# 2) 建立專案（若已存在則略過）
$slug = "ggk7015-mod"
$projectBody = @{
    name         = "GOC Anomaly Strike"
    slug         = $slug
    summary      = "GOC anti-anomaly blade: applies Anomaly Suppression (slow + attack down) with glow on hit."
    description  = "A GOC (Global Occult Coalition) themed sword that applies the **Anomaly Suppression** status effect on hit, reducing the target's movement speed and attack damage while making it glow. Includes end-rod particles and blaze sound, a crafting recipe, full Traditional-Chinese + English lang, and an item model. Source: <https://github.com/ggk7015/ggk7015-mod>"
    license      = @{ id = "MIT" }
    team_id      = $me.id
    project_type = "mod"
    game_versions = @($GameVersion)
    loaders      = @("fabric")
    body_url     = $null
    source_url   = "https://github.com/ggk7015/ggk7015-mod"
} | ConvertTo-Json -Depth 5

try {
    $project = Invoke-RestMethod "$api/project" -Method Post -Headers $headers -ContentType "application/json" -Body $projectBody
    Write-Host "專案已建立：$($project.title)  $($api.Replace('/v2',''))/project/$($project.slug)"
} catch {
    if ($_.Exception.Response.StatusCode -eq 409) {
        $project = Invoke-RestMethod "$api/project/$slug" -Headers $headers
        Write-Host "專案已存在，沿用：$($project.id)"
    } else { throw }
}

# 3) 發布版本（含 jar 上傳）
$versionName = "1.0.0"
$featured = $false
$versionBody = @{
    name            = $versionName
    version_number  = $versionName
    changelog       = "Initial release: GOC Anomaly Strike sword + Anomaly Suppression status effect (+25% slow / -20% attack) with glow, particles, sound, crafting recipe, and zh_tw/en_us lang."
    dependencies    = @(@{ project_id = "fabric-api"; dependency_type = "required" })
    game_versions   = @($GameVersion)
    version_type    = "release"
    loaders         = @("fabric")
    featured        = $featured
    project_id      = $project.id
    file_parts      = @("ggk7015-mod-1.0.0.jar")
    primary_file    = "ggk7015-mod-1.0.0.jar"
    release_channel = "release"
} | ConvertTo-Json -Depth 6

# multipart：JSON 欄位 + 檔
$boundary = [Guid]::NewGuid().ToString("N")
$body = New-Object System.IO.MemoryStream
$sw = New-Object System.IO.StreamWriter($body, [Text.Encoding]::UTF8)
$sw.Write("--$boundary`r`n")
$sw.Write('Content-Disposition: form-data; name="data"`r`nContent-Type: application/json`r`n`r`n')
$sw.Write($versionBody)
$sw.Write("`r`n--$boundary`r`n")
$sw.Write('Content-Disposition: form-data; name="primary_file"; filename="ggk7015-mod-1.0.0.jar"`r`nContent-Type: application/octet-stream`r`n`r`n')
$sw.Flush()
$fileBytes = [IO.File]::ReadAllBytes((Resolve-Path $JarPath))
$body.Write($fileBytes, 0, $fileBytes.Length)
$sw.Write("`r`n--$boundary--`r`n")
$sw.Flush()

$version = Invoke-RestMethod "$api/version" -Method Post -Headers $headers `
    -ContentType "multipart/form-data; boundary=$boundary" -Body $body.ToArray()

Write-Host "發布完成！"
Write-Host "  專案頁 : https://modrinth.com/project/$($project.slug)"
Write-Host "  版本頁 : https://modrinth.com/mod/$($project.slug)/version/$($version.id)"
Write-Host "下一步 : 回 Hackcraft 提交表單貼上專案頁，官方即發放 Minecraft Java 帳號。"
