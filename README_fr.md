<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 [Español](README_es.md) · 🇫🇷 **Français** · 🇲🇾 [Bahasa Melayu](README_ms.md)

# 🍎 Edible Potions

**Infusez de la magie dans votre nourriture — mangez une pomme, gagnez Vision Nocturne instantanément !**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## Aperçu

**Edible Potions** ajoute une **Table d'Infusion** à Minecraft, un nouvel établi qui infuse les effets de potion directement dans la nourriture. Plus besoin de chercher des fioles en plein combat — mangez une Pomme de Vitesse, un Steak de Force ou un Pain de Soin et obtenez les effets de potion instantanément.

Entièrement compatible avec **Forge** et **Fabric** sur Minecraft **1.20.1**, construit avec le framework multi-loader [Architectury](https://architectury.dev/) pour une parité fonctionnelle parfaite sur les deux plateformes.

---

## Fonctionnalités

### 🔧 Table d'Infusion
Un nouveau bloc d'établi fabriqué à partir d'Or, de Diamant, de Fioles en Verre et d'Obsidienne. Placez la nourriture dans l'emplacement de gauche, une potion (normale / jetable / persistante) au centre, et attendez que l'infusion se termine.

### 🧪 Trois Modes de Distribution
| Mode | Potion Source | Effet en Mangeant |
|----------|--------------|---------------|
| **Normal** | `minecraft:potion` | Effets appliqués uniquement au mangeur |
| **Jetable** | `minecraft:splash_potion` | Effets projetés sur toutes les entités dans **3 blocs** |
| **Persistant** | `minecraft:lingering_potion` | Laisse un **Nuage d'Effet** à la position du mangeur |

Les trois variantes apparaissent comme des objets distincts dans l'onglet créatif avec des noms différenciés
(ex. *Pomme de Vision Nocturne*, *Pomme Jetable de Vision Nocturne*, *Pomme Persistante de Vision Nocturne*).

### ⚡ Contrôle Redstone
Appliquer un signal de redstone à la Table d'Infusion **interrompt** le processus d'infusion. Retirez le signal pour reprendre.
Combiné avec la sortie du comparateur (0–15 selon la progression), vous pouvez construire des lignes de production entièrement automatisées.

### 🔄 Automatisation par Entonnoir
| Face | Comportement |
|--------|----------|
| **Dessus** | Entrée de **nourriture** (tout objet comestible) |
| **Côtés** | Entrée de **potions** (normale / jetable / persistante) |
| **Dessous** | Extraction du **résultat** (nourriture infusée) et des **fioles en verre vides** |

La Table d'Infusion implémente `WorldlyContainer` — compatible avec les entonnoirs vanilla, les tuyaux de mods, et tout transport d'objets respectant l'API `IItemHandler` / `SidedInventory`.

### 🎨 Finitions Visuelles et Audio
- **Textures style IC2** — boîtier métallique sombre avec accents d'énergie orange et détails de rivets
- **Particules colorées en mangeant** — des particules de poussière aux couleurs de la potion jaillissent du joueur
- **Son d'artisanat** — son de l'alambic à la fin de l'infusion
- **Brillance enchantée** sur tous les objets de nourriture infusée
- **Noms d'objets dynamiques** — ex. "Côtelette de Porc de Vitesse II", "Pomme Dorée Jetable de Soin"

### 💀 Persistance des Effets à la Mort *(v1.0.2)*
Les effets de potion actifs de la nourriture infusée sont **préservés après la mort**.
Après réapparition, les effets sont restaurés avec une durée maximale de **3 minutes**,
pour ne pas perdre vos précieux buffs sans être trop puissants.

### 🌐 Multiplateforme (Forge & Fabric)
Toute la logique de jeu réside dans le module `common` partagé. Le code spécifique à la plateforme se limite aux classes de démarrage, à la génération de données et à l'enregistrement des Mixins. Les transformateurs bytecode d'Architectury gèrent le reste à la compilation.

---

## Nourritures Supportées

Les **40 nourritures vanilla** sont supportées (Gâteau et Seau de Lait exclus) :

`Pomme` · `Pomme dorée` · `Pomme dorée enchantée` · `Tranche de pastèque` · `Baies sucrées` ·
`Baies lumineuses` · `Chorus` · `Carotte` · `Carotte dorée` · `Pomme de terre` · `Pomme de terre cuite` ·
`Pomme de terre empoisonnée` · `Betterave` · `Laminaire séchée` · `Bœuf cru` · `Steak` · `Côtelette crue` ·
`Côtelette cuite` · `Mouton cru` · `Mouton cuit` · `Poulet cru` · `Poulet rôti` · `Lapin cru` ·
`Lapin cuit` · `Chair putréfiée` · `Morue crue` · `Morue cuite` · `Saumon cru` · `Saumon cuit` ·
`Poisson tropical` · `Poisson-globe` · `Pain` · `Cookie` · `Tarte à la citrouille` · `Soupe de champignons` ·
`Soupe de betteraves` · `Ragoût de lapin` · `Soupe suspecte` · `Fiole de miel` · `Œil d'araignée`

× tous les effets de potion valides × 3 modes de distribution = **~4 500 nourritures infusées**

---

## Recette de la Table d'Infusion

```
┌───┬───┬───┐
│   │ O │   │    O = Lingot d'Or
├───┼───┼───┤    F = Fiole en Verre
│ F │ D │ F │    D = Diamant
├───┼───┼───┤    Ob = Obsidienne
│ Ob│ Ob│ Ob│
└───┴───┴───┘
```

La recette se débloque dans le livre de recettes lorsque vous obtenez un **Lingot d'Or**.

---

## Installation

1. Téléchargez le `.jar` pour votre plateforme depuis [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions), ou [Modrinth](https://modrinth.com/mod/ediblepotions)
2. Placez-le dans votre dossier `mods`
3. Lancez le jeu

### Dépendances

| Plateforme | Requis |
|----------|----------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## Compilation depuis les Sources

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # Compile les deux plateformes
./gradlew :forge:runData      # Régénère les modèles d'objets (après modification de recettes/objets)
./gradlew :forge:runClient    # Lance le client de développement Forge
./gradlew :fabric:runClient   # Lance le client de développement Fabric
```

Nécessite **JDK 17**.

---

## Licence

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Fait avec ❤️ par <b>lingyunmo</b> · Construit avec <a href="https://architectury.dev/">Architectury</a>
</div>
