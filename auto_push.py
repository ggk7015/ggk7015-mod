import time
import subprocess
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

class GitAutoPushHandler(FileSystemEventHandler):
    def __init__(self):
        self.last_commit = 0
        self.cooldown = 10  # 10 秒冷卻時間，避免頻繁存檔導致提交過於密集

    def on_modified(self, event):
        # 忽略 .git 資料夾與暫存檔
        if ".git" in event.src_path or event.is_directory:
            return

        current_time = time.time()
        if current_time - self.last_commit > self.cooldown:
            self.last_commit = current_time
            print("檢測到檔案變更，準備自動 Push...")
            try:
                subprocess.run(["git", "add", "."], check=True)
                subprocess.run(["git", "commit", "-m", "auto: update code"], check=True)
                subprocess.run(["git", "push"], check=True)
                print("成功自動 Push 到 GitHub！")
            except subprocess.CalledProcessError as e:
                print(f"Git 操作失敗或無內容變更: {e}")

if __name__ == "__main__":
    path = "."  # 監控當前資料夾
    event_handler = GitAutoPushHandler()
    observer = Observer()
    observer.schedule(event_handler, path, recursive=True)
    observer.start()
    print("自動 Commit & Push 監控中... (按 Ctrl+C 結束)")
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()