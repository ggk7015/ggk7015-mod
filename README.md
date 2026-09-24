# ggk7015-mod

A Minecraft 26.2 Fabric mod adding one GOC-themed weapon and status effect,
adapted from the Global Occult Coalition (SCP: Secret Laboratory) setting.

## Requirements

- Minecraft `~26.2`
- Fabric Loader + Fabric API
- Java 25

## Contents

### GOC Anomaly Strike (`goc_strike`)

Diamond-tier anti-anomaly blade crafted from gold ingots, a netherite ingot,
and an ender pearl. On hit it applies:

- **Anomaly Suppression** (5s) — movement speed −15%, attack damage −20%
- **Glowing** (5s)

Anomaly Suppression also makes the target emit end-rod particles while active.

Give it to yourself in game:

```
/give @s ggk7015-mod:goc_strike
```

### Anomaly Suppression effect (`anomaly_suppression`)

A harmful effect registered under this mod's namespace. Attribute modifiers
are applied via the 26.2 `MobEffect` API, with per-tick particles handled in
`applyEffectTick`.

## Building

```
.\gradlew.bat build
```

Output jar: `build/libs/ggk7015-mod-1.0.0.jar`

To test locally:

```
.\gradlew.bat runClient
```

## Project layout

```
src/main/java/io/github/ggk7015/ggk7015mod/
  Main.java                    # entrypoint, registers content
  ModItems.java                # item registry
  item/GocStrikeItem.java      # weapon behaviour
  effect/
    ModEffects.java            # effect registry
    AnomalySuppressionEffect.java
src/main/resources/
  assets/ggk7015-mod/lang/     # en_us + zh_tw
  assets/ggk7015-mod/models/   # item model
  data/ggk7015-mod/recipe/     # crafting recipe
```

## License

MIT
