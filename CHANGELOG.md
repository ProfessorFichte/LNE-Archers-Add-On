# Changelog

All notable changes to LNE Archers Add-On are documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.3.1+1.21.1]

### Added

- The Elder Guardian Bow, Long Bow, Rapid Crossbow and Heavy Crossbow shoot normally underwater (needs More RPG Library 2.8.3)

### Changed

- Works with Loot & Explore 1.1.0 (the 1.21.1 rewrite) on Fabric and NeoForge
- Needs More RPG Library 2.8.4 or newer
- Loot tables are now made by datagen instead of hand-written files, the loot stays the same
### Fixed

- The Frozen Slave sound is now 48 kHz like the other sounds
- Deadeye tier 1 chests can drop Rogue spell scrolls again, the spell pool id was wrong
- Tundra Hunter tier 5 chests can drop the Diamond Berserker Axe (Berserker installed), the item id was wrong
- Polar Bear Fur in the item frames of the Tundra Hunter hut and outpost shows up again, and the armor stand in the Archers camp keeps its Damage Reflect bonus (old More RPG Library ids)
- The LNE spears swing like spears again with Better Combat
- With Better Combat, the LNE Bows and Rapid Crossbows no longer point to Better Combat presets that don't exist and behave like the Archers bows they upgrade from
- Ranger treasure sites can spawn in all forest biomes again, modded ones included (the forest biome tag id was out of date)

## [1.3.0+1.21.1]

### Changed

- Adopt Spell Engine 1.10.9 & More RPG Library 2.8.0
- **BREAKING for modpack/datapack developers:** More RPG Library's namespace was renamed `more_rpg_classes` -> `mrpg_lib` (and its advancement root moved to `mrpg_lib:root`). If you have any data files, loot tables, tags or advancements that reference the old `more_rpg_classes:`/`more_rpg_content:` ids from this mod or MRPGL, update them to `mrpg_lib:`
- Updated internal advancement parents, mod dependency declarations (fabric.mod.json, neoforge.mods.toml)

## [1.2.2+1.21.1]

### Fixed

- fixed a crash when opening creative inventory

## [1.2.1+1.21.1]

### Changed

- Drop Forgified Fabric API (FFAPI) as a required dependency
- Updated some Spell Textures for LNE Spells (created by Slepykat)

### Fixed

- Fixed a crash when opening the creative inventory without Loot & Explore installed

## [1.2.0+1.21.1]

### Changed

- Adopt Spell Engine 1.10
- Improve FrozenSlave checking if the entity was killed by the Caster that inflicted "WintersGrip"

## [1.1.3+1.21.1]

### Changed

- Infiltrators Arrow, Winters Grip & Fan of Fire are now available in the LNE Archers Mod!
- Changed Infiltrator's Arrow & Winter's Grip
- Infiltrators Arrow: A charged spell, that shoots an arrow that flies further the longer you charge. You'll get teleported to the location where the arrow hits.
- Winters Grip: Still a cloud that slows enemy's. Targets that are killed in the Cloud, become your Frozen Slaves fighting for you.
- Fan of Fire: unchanged

## [1.1.2+1.21.1]

### Added

- Add a proper Spell Tooltip for Rangers Focus

### Changed

- Project Clean Up

### Fixed

- Fix Crash on Neoforge

## [1.1.1+1.21.1]

**Balancing & Internal Changes:**

### Changed

- Adapt to Spell Engine 1.9.10+ API Changes
- Rangers focus is now a charging spell, the longer you charge, the higher the buff effect amplifier gets

## [1.1.0+1.21.1]

**Update to use Spell Engine 1.9.0**

### Changed

- DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes.
- All Additional Spells are now Tier 5 Spell's
- The Spells can now also be learned in the Spell Binding Table
- Small tweaks in the Loot Tables
- Moved all the structures from loot_n_explore to lne_archers, so it's clearer that these structures come from this add-on
- Arrows show with Rangers Focus now creates a Nature Spell-Damage Area Impact, that scales with Ranged Damage
- Rangers Focus is now instantly casted

## [1.0.9+1.21.1]

### Fixed

- fix the log spam from wrong particle spawner for Ranger's focus

## [1.0.8+1.21.1]

### Changed

- Move to Architectury Enviroment for Multiloader
- NeoForge Beta!
- Bows and Crossbows now also come with Spell Power for their Weapon Theme!
- Buff Spell Power for Weapons

## [1.0.7+1.21.1]

### Added

- add missing filled map lang translation

### Changed

- use new loot function for loot chests: "more_rpg_classes:specific_spell_scroll_pool"

### Fixed

- fix crash with Glacial Splitter Passive without Archers Expansion installed

## [1.0.6+1.21.1]

### Changed

- Rangers Focus is now a T4 spell
- it can now also be looted outside the class structure
- Also Buffed Rangers Focus, it now also creates a small damage area impact on arrow impact

## [1.0.5+1.21.1]

### Changed

- Spell Engine 1.7 Update

## [1.0.4+1.21.1]

### Fixed

- fix ranged weapon config
- fix issue with fabric-mod.json

## [1.0.3+1.21.1]

### Changed

- renamed the Ender Dragon Heavy Crossbow
- Update Mod Icon
- Update License

## [1.0.2+1.21.1]

### Added

- add Loot & Explore Weapon Theme Tags

## [1.0.1+1.21.1]

### Fixed

- fix some target modifiers in Passive Spell Impact

## [1.0.0+1.21.1]

### Changed

**Official 1.21.1 Release!**

- Passive Spells For the Weapons are now handled with the new Spell Engine Passive API
- Rangers focus will now decrease your pull time, and increase your arrow velocity
- the movement speed when Rangers Focus is active was reduced more
- The special Spells can now be found as a spell scroll
- The Class related structures will now contain spell scrolls in their loot chests
- Made some small loot table tweaks
