# 1.2.2+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.**
> - **Update the whole set together.** Mixing in an older add-on will break at startup or misbehave in play.
>
> **Back up your world before updating.**

- Ported to Minecraft 1.20.1 (Fabric + Forge 47). NeoForge is replaced by Forge on this line, the same Forge jar also loads on NeoForge 1.20.1.
- Requires the matching 1.20.1 releases of Spell Engine (1.10.5), Spell Power (1.6.0), More RPG Library (2.7.2), Archers (3.1.1) and Ranged Weapon API (2.3.4). Loot & Explore (1.0.22) stays optional.
- Loot & Explore has no Forge build, so on Forge the bows, crossbows and spears need it and are skipped without it, the spells, effects and entities work either way.
- The Archers Expansion compat pack is still included and only loads when Archers Expansion is installed.
