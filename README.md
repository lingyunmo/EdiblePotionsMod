<div align="center">

# 🍎 Edible Potions

**Infuse your food with magic — eat an apple, gain Night Vision instantly!**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

[简体中文](README_zh.md)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## Overview

**Edible Potions** adds an **Infusion Table** to Minecraft, a new workstation that infuses potion effects directly into food. No more fumbling with bottles mid-combat — eat a Speed Apple, a Strength Steak, or a Healing Bread and gain the corresponding potion effects instantly.

Fully compatible with **Forge** and **Fabric** on Minecraft **1.20.1**, built with the [Architectury](https://architectury.dev/) multi-loader framework for feature parity across both platforms.

---

## Features

### 🔧 Infusion Table
A new workstation block crafted from Gold, Diamond, Glass Bottles, and Obsidian. Place food in the left slot, any potion (regular / splash / lingering) in the center, and wait for the infusion to complete.

### 🧪 Three Delivery Modes
| Delivery | Source Potion | Eating Effect |
|----------|--------------|---------------|
| **Regular** | `minecraft:potion` | Effects applied to the eater only |
| **Splash** | `minecraft:splash_potion` | Effects burst outward to all entities within **3 blocks** |
| **Lingering** | `minecraft:lingering_potion` | Leaves an **Area Effect Cloud** at the eater's position |

All three variants appear as separate items in the creative tab with distinct names
(e.g. *Apple of Night Vision*, *Splash Apple of Night Vision*, *Lingering Apple of Night Vision*).

### ⚡ Redstone Control
Applying a redstone signal to the Infusion Table **pauses** the infusion process. Remove the signal to resume.
Combined with the comparator output (0–15 based on progress), you can build fully automated production lines.

### 🔄 Hopper Automation
| Face   | Behavior |
|--------|----------|
| **Top** | Input **food** (any edible item) |
| **Sides** | Input **potions** (regular / splash / lingering) |
| **Bottom** | Extract **output** (infused food) and **empty glass bottles** |

The Infusion Table implements `WorldlyContainer` — compatible with vanilla hoppers, modded pipes, and any item transport that respects the `IItemHandler` / `SidedInventory` API.

### 🎨 Visual & Audio Polish
- **IC2-style textures** — dark machine casing with orange energy accents and rivet details
- **Colored particles** on eating — potion-colored dust particles burst from the player
- **Crafting sound** — brewing-stand brew sound on infusion completion
- **Enchanted glint** on all infused food items
- **Dynamic item names** — e.g. "Swiftness II Cooked Porkchop", "Splash Healing Golden Apple"

### 💀 Death Effect Persistence *(v1.0.2)*
Active potion effects from infused food are **preserved across death**.
After respawning, effects are restored with a maximum duration of **3 minutes**,
so you don't lose your hard-earned buffs but aren't overpowered either.

### 🌐 Cross-Platform (Forge & Fabric)
All game logic lives in the shared `common` module. Platform-specific code is
limited to bootstrap classes, data generation, and mixin registration.
Architectury bytecode transformers handle the rest at build time.

---

## Supported Foods

All **47 vanilla foods** are supported (Cake, Milk Bucket, and Ominous Bottle excluded):

`Apple` · `Golden Apple` · `Enchanted Golden Apple` · `Melon Slice` · `Sweet Berries` ·
`Glow Berries` · `Chorus Fruit` · `Carrot` · `Golden Carrot` · `Potato` · `Baked Potato` ·
`Poisonous Potato` · `Beetroot` · `Dried Kelp` · `Beef` · `Cooked Beef` · `Porkchop` ·
`Cooked Porkchop` · `Mutton` · `Cooked Mutton` · `Chicken` · `Cooked Chicken` · `Rabbit` ·
`Cooked Rabbit` · `Rotten Flesh` · `Cod` · `Cooked Cod` · `Salmon` · `Cooked Salmon` ·
`Tropical Fish` · `Pufferfish` · `Bread` · `Cookie` · `Pumpkin Pie` · `Mushroom Stew` ·
`Beetroot Soup` · `Rabbit Stew` · `Suspicious Stew` · `Honey Bottle` · `Spider Eye`

× all valid potion effects × 3 delivery modes = **~4,500 infused food items**

---

## Infusion Table Recipe

```
┌───┬───┬───┐
│   │ G │   │    G = Gold Ingot
├───┼───┼───┤    B = Glass Bottle
│ B │ D │ B │    D = Diamond
├───┼───┼───┤    O = Obsidian
│ O │ O │ O │
└───┴───┴───┘
```

The recipe unlocks in the vanilla recipe book when you obtain a **Gold Ingot**.

---

## Installation

1. Download the `.jar` for your platform from [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions), or [Modrinth](https://modrinth.com/mod/ediblepotions)
2. Drop it into your `mods` folder
3. Launch the game

### Dependencies

| Platform | Required |
|----------|----------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## Building from Source

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build          # Build both platforms
./gradlew :forge:runData  # Regenerate item models (after changing recipes/items)
./gradlew :forge:runClient  # Launch Forge dev client
./gradlew :fabric:runClient # Launch Fabric dev client
```

Requires **JDK 17**.

---

## License

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Made with ❤️ by <b>lingyunmo</b> · Built on <a href="https://architectury.dev/">Architectury</a>
</div>
