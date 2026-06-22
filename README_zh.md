<div align="center">

# 🍎 Edible Potions · 食用药剂

**给你的食物注入魔法 — 吃颗苹果，瞬间获得夜视！**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 **简体中文** · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 [Español](README_es.md) · 🇫🇷 [Français](README_fr.md) · 🇲🇾 [Bahasa Melayu](README_ms.md)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## 概述

**Edible Potions（食用药剂）** 为 Minecraft 添加了一个全新的工作站——**注能台**，能将药水效果"注入"食物中。告别战斗中手忙脚乱喝药的尴尬——吃一颗迅捷苹果、一块力量牛排、一片治疗面包，即刻获得对应药水效果。

同时支持 **Forge** 和 **Fabric**，基于 [Architectury](https://architectury.dev/) 多加载器框架构建，双平台行为完全一致。

---

## 功能特性

### 🔧 注能台
由金锭、钻石、玻璃瓶和黑曜石合成的新工作站。左侧放入食物，中间放入任意药水（普通/喷溅/滞留），等待注能完成即可。

### 🧪 三种递送方式
| 递送方式 | 来源药水 | 食用效果 |
|----------|---------|---------|
| **普通** | `minecraft:potion`（可饮用） | 仅对食用者生效 |
| **喷溅** | `minecraft:splash_potion` | 效果溅射到周围 **3 格**内所有生物 |
| **滞留** | `minecraft:lingering_potion` | 在食用者位置留下一片**药水云** |

三种变体在创造模式物品栏中以独立物品出现，名字明确区分
（如：*夜视苹果*、*喷溅夜视苹果*、*滞留夜视苹果*）。

### ⚡ 红石控制
向注能台输入红石信号可**暂停**注能进程，撤去信号后恢复。
配合比较器输出（基于进度 0–15 级），可构建全自动生产线。

### 🔄 漏斗自动化
| 方向 | 行为 |
|------|------|
| **上方** | 输入**食物**（任意可食用物品） |
| **侧面** | 输入**药水**（普通/喷溅/滞留均可） |
| **下方** | 提取**成品**（注能食物）和**空玻璃瓶** |

注能台实现了 `WorldlyContainer` 接口——兼容原版漏斗、模组管道，及任何遵循 `IItemHandler` / `SidedInventory` API 的物流系统。

### 🎨 视听效果
- **IC2 工业风格材质** — 暗金属外壳 + 橙色能量指示灯 + 铆钉细节
- **食用彩色粒子** — 根据药水颜色生成对应色粉尘粒子
- **完成音效** — 注能完成时播放酿造台音效
- **附魔光泽** — 所有注能食物都有附魔光效
- **动态物品名** — 如"迅捷 II 熟猪排"、"喷溅治疗金苹果"

### 💀 死亡保留效果 *(v1.0.2)*
来自注能食物的活跃药水效果在死亡后**保留至重生**。
重生后恢复的效果最长持续 **3 分钟**，既不会白费辛苦攒的 Buff，也不会过于强力。

### 🌐 跨平台（Forge & Fabric）
所有游戏逻辑集中在 `common` 模块，平台特定代码仅限于启动类、数据生成和 Mixin 注册。Architectury 字节码转换器在构建时自动处理平台差异。

---

## 支持的食物

支持全部 **40 种原版食物**（蛋糕、奶桶除外）：

`苹果` · `金苹果` · `附魔金苹果` · `西瓜片` · `甜浆果` · `发光浆果` ·
`紫颂果` · `胡萝卜` · `金胡萝卜` · `马铃薯` · `烤马铃薯` · `毒马铃薯` ·
`甜菜根` · `干海带` · `生牛肉` · `牛排` · `生猪排` · `熟猪排` · `生羊肉` ·
`熟羊肉` · `生鸡肉` · `熟鸡肉` · `生兔肉` · `熟兔肉` · `腐肉` · `生鳕鱼` ·
`熟鳕鱼` · `生鲑鱼` · `熟鲑鱼` · `热带鱼` · `河豚` · `面包` · `曲奇` ·
`南瓜派` · `蘑菇煲` · `甜菜汤` · `兔肉煲` · `迷之炖菜` · `蜂蜜瓶` · `蜘蛛眼`

× 所有有效药水效果 × 3 种递送方式 = **约 4,500 种注能食物**

---

## 注能台合成配方

```
┌───┬───┬───┐
│   │金锭│   │
├───┼───┼───┤
│玻璃│钻石│玻璃│
│瓶  │   │瓶  │
├───┼───┼───┤
│黑曜│黑曜│黑曜│
│石  │石  │石  │
└───┴───┴───┘
```

获得**金锭**后，配方会在原版配方书中自动解锁。

---

## 安装说明

1. 从 [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases)、[CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions) 或 [Modrinth](https://modrinth.com/mod/ediblepotions) 下载对应平台的 `.jar`
2. 放入 `mods` 文件夹
3. 启动游戏

### 依赖

| 平台 | 需要安装 |
|------|---------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## 从源码构建

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # 构建双平台
./gradlew :forge:runData      # 重新生成物品模型（修改配方/物品后）
./gradlew :forge:runClient    # 启动 Forge 开发客户端
./gradlew :fabric:runClient   # 启动 Fabric 开发客户端
```

需要 **JDK 17**。

---

## 开源协议

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Made with ❤️ by <b>lingyunmo</b> · Built on <a href="https://architectury.dev/">Architectury</a>
</div>
