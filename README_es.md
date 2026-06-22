<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 **Español** · 🇫🇷 [Français](README_fr.md) · 🇲🇾 [Bahasa Melayu](README_ms.md)

# 🍎 Edible Potions

**¡Infunde magia en tu comida — come una manzana y obtén Visión Nocturna al instante!**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## Resumen

**Edible Potions** añade una **Mesa de Infusión** a Minecraft, una nueva estación de trabajo que infunde efectos de poción directamente en la comida. No más buscar frascos en pleno combate — come una Manzana de Velocidad, un Filete de Fuerza o un Pan de Curación y obtén los efectos de poción al instante.

Totalmente compatible con **Forge** y **Fabric** en Minecraft **1.20.1**, construido con el framework multi-loader [Architectury](https://architectury.dev/) para total paridad de funciones en ambas plataformas.

---

## Características

### 🔧 Mesa de Infusión
Un nuevo bloque de trabajo fabricado con Oro, Diamante, Botellas de Vidrio y Obsidiana. Pon comida en la ranura izquierda, cualquier poción (normal / salpicadura / persistente) en el centro, y espera a que se complete la infusión.

### 🧪 Tres Modos de Entrega
| Modo | Poción Fuente | Efecto al Comer |
|----------|--------------|---------------|
| **Normal** | `minecraft:potion` | Efectos aplicados solo al que come |
| **Salpicadura** | `minecraft:splash_potion` | Efectos se expanden a todas las entidades en **3 bloques** |
| **Persistente** | `minecraft:lingering_potion` | Deja una **Nube de Efecto** en la posición del que come |

Las tres variantes aparecen como objetos separados en la pestaña creativa con nombres distintos
(ej. *Manzana de Visión Nocturna*, *Manzana de Salpicadura de Visión Nocturna*, *Manzana Persistente de Visión Nocturna*).

### ⚡ Control con Redstone
Aplicar una señal de redstone a la Mesa de Infusión **pausa** el proceso de infusión. Retira la señal para reanudar.
Combinado con la salida del comparador (0–15 basado en el progreso), puedes construir líneas de producción completamente automatizadas.

### 🔄 Automatización con Tolvas
| Cara | Comportamiento |
|--------|----------|
| **Superior** | Introduce **comida** (cualquier objeto comestible) |
| **Lados** | Introduce **pociones** (normal / salpicadura / persistente) |
| **Inferior** | Extrae **resultado** (comida infundida) y **botellas de vidrio vacías** |

La Mesa de Infusión implementa `WorldlyContainer` — compatible con tolvas vanilla, tuberías de mods, y cualquier transporte de objetos que respete la API `IItemHandler` / `SidedInventory`.

### 🎨 Detalles Visuales y de Audio
- **Texturas estilo IC2** — carcasa metálica oscura con acentos de energía naranja y remaches
- **Partículas de colores al comer** — partículas de polvo del color de la poción brotan del jugador
- **Sonido de elaboración** — sonido del soporte de pociones al completar la infusión
- **Brillo encantado** en todos los objetos de comida infundida
- **Nombres dinámicos** — ej. "Chuleta de Velocidad II", "Manzana Dorada de Salpicadura de Curación"

### 💀 Persistencia de Efectos al Morir *(v1.0.2)*
Los efectos de poción activos de la comida infundida se **conservan tras la muerte**.
Después de reaparecer, los efectos se restauran con una duración máxima de **3 minutos**,
así no pierdes tus mejoras ganadas pero tampoco son excesivamente poderosas.

### 🌐 Multiplataforma (Forge & Fabric)
Toda la lógica del juego reside en el módulo `common` compartido. El código específico de plataforma se limita a clases de arranque, generación de datos y registro de Mixins. Los transformadores de bytecode de Architectury manejan el resto en tiempo de compilación.

---

## Comidas Soportadas

Las **40 comidas vanilla** son compatibles (Tarta y Cubo de Leche excluidos):

`Manzana` · `Manzana dorada` · `Manzana dorada encantada` · `Rodaja de sandía` · `Bayas dulces` ·
`Bayas luminosas` · `Fruta chorus` · `Zanahoria` · `Zanahoria dorada` · `Patata` · `Patata asada` ·
`Patata venenosa` · `Remolacha` · `Algas secas` · `Ternera cruda` · `Filete` · `Chuleta cruda` ·
`Chuleta cocinada` · `Cordero crudo` · `Cordero cocinado` · `Pollo crudo` · `Pollo cocinado` ·
`Conejo crudo` · `Conejo cocinado` · `Carne podrida` · `Bacalao crudo` · `Bacalao cocinado` ·
`Salmón crudo` · `Salmón cocinado` · `Pez tropical` · `Pez globo` · `Pan` · `Galleta` ·
`Pastel de calabaza` · `Estofado de setas` · `Sopa de remolacha` · `Estofado de conejo` ·
`Estofado sospechoso` · `Botella de miel` · `Ojo de araña`

× todos los efectos de poción válidos × 3 modos de entrega = **~4,500 comidas infundidas**

---

## Receta de la Mesa de Infusión

```
┌───┬───┬───┐
│   │ G │   │    G = Lingote de Oro
├───┼───┼───┤    B = Botella de Vidrio
│ B │ D │ B │    D = Diamante
├───┼───┼───┤    O = Obsidiana
│ O │ O │ O │
└───┴───┴───┘
```

La receta se desbloquea en el libro de recetas al obtener un **Lingote de Oro**.

---

## Instalación

1. Descarga el `.jar` para tu plataforma desde [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions), o [Modrinth](https://modrinth.com/mod/ediblepotions)
2. Colócalo en tu carpeta `mods`
3. Inicia el juego

### Dependencias

| Plataforma | Requerido |
|----------|----------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## Compilar desde Código Fuente

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # Compila ambas plataformas
./gradlew :forge:runData      # Regenera modelos de objetos (tras cambiar recetas/objetos)
./gradlew :forge:runClient    # Inicia cliente de desarrollo Forge
./gradlew :fabric:runClient   # Inicia cliente de desarrollo Fabric
```

Requiere **JDK 17**.

---

## Licencia

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Hecho con ❤️ por <b>lingyunmo</b> · Construido con <a href="https://architectury.dev/">Architectury</a>
</div>
