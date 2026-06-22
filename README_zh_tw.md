<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 **繁體中文** · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 [Español](README_es.md) · 🇫🇷 [Français](README_fr.md) · 🇲🇾 [Bahasa Melayu](README_ms.md)

# 🍎 Edible Potions · 食用藥水

**將魔法注入你的食物 — 吃顆蘋果，立刻獲得夜視！**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## 概述

**Edible Potions（食用藥水）** 為 Minecraft 新增了一個全新的工作站——**灌注台（Infusion Table）**，能將藥水效果「注入」到食物中。告別在戰鬥中手忙腳亂喝藥水的窘境 — 吃一顆迅捷蘋果、一塊力量牛排、一片治療麵包，即刻獲得對應藥水效果。

同時支援 **Forge** 和 **Fabric**，基於 [Architectury](https://architectury.dev/) 多載入器框架構建，雙平台行為完全一致。

---

## 功能特性

### 🔧 灌注台
由金錠、鑽石、玻璃瓶和黑曜石合成的新工作站。左側放入食物，中間放入任意藥水（普通／飛濺／滯留），等待灌注完成即可。

### 🧪 三種遞送方式
| 遞送方式 | 來源藥水 | 食用效果 |
|----------|---------|---------|
| **普通** | `minecraft:potion`（可飲用） | 僅對食用者生效 |
| **飛濺** | `minecraft:splash_potion` | 效果濺射到周圍 **3 格**內所有生物 |
| **滯留** | `minecraft:lingering_potion` | 在食用者位置留下一片**藥水雲** |

三種變體在創造模式物品欄中以獨立物品出現，名稱明確區分
（如：*夜視蘋果*、*飛濺夜視蘋果*、*滯留夜視蘋果*）。

### ⚡ 紅石控制
向灌注台輸入紅石訊號可**暫停**灌注進程，撤去訊號後恢復。
配合比較器輸出（基於進度 0–15 級），可構建全自動生產線。

### 🔄 漏斗自動化
| 方向 | 行為 |
|------|------|
| **上方** | 輸入**食物**（任意可食用物品） |
| **側面** | 輸入**藥水**（普通／飛濺／滯留均可） |
| **下方** | 提取**成品**（灌注食物）和**空玻璃瓶** |

灌注台實作了 `WorldlyContainer` 介面——相容原版漏斗、模組管道，及任何遵循 `IItemHandler` / `SidedInventory` API 的物流系統。

### 🎨 視聽效果
- **IC2 工業風格材質** — 暗金屬外殼 + 橙色能量指示燈 + 鉚釘細節
- **食用彩色粒子** — 根據藥水顏色生成對應色粉塵粒子
- **完成音效** — 灌注完成時播放釀造台音效
- **附魔光澤** — 所有灌注食物都有附魔光效
- **動態物品名** — 如「迅捷 II 烤豬肉」、「飛濺治療金蘋果」

### 💀 死亡保留效果 *(v1.0.2)*
來自灌注食物的活躍藥水效果在死亡後**保留至重生**。
重生後恢復的效果最長持續 **3 分鐘**，既不會白費辛苦攢的 Buff，也不會過於強力。

### 🌐 跨平台（Forge & Fabric）
所有遊戲邏輯集中在 `common` 模組，平台特定程式碼僅限於啟動類別、資料生成和 Mixin 註冊。Architectury 位元組碼轉換器在建置時自動處理平台差異。

---

## 支援的食物

支援全部 **40 種原版食物**（蛋糕、牛奶桶除外）：

`蘋果` · `金蘋果` · `附魔金蘋果` · `西瓜片` · `甜莓` · `螢光莓` ·
`歌萊果` · `胡蘿蔔` · `金胡蘿蔔` · `馬鈴薯` · `烤馬鈴薯` · `毒馬鈴薯` ·
`甜菜根` · `海帶乾` · `生牛肉` · `牛排` · `生豬肉` · `烤豬肉` · `生羊肉` ·
`烤羊肉` · `生雞肉` · `烤雞肉` · `生兔肉` · `烤兔肉` · `腐肉` · `生鱈魚` ·
`烤鱈魚` · `生鮭魚` · `烤鮭魚` · `熱帶魚` · `河豚` · `麵包` · `餅乾` ·
`南瓜派` · `蘑菇湯` · `甜菜湯` · `兔肉湯` · `可疑燉湯` · `蜂蜜瓶` · `蜘蛛眼`

× 所有有效藥水效果 × 3 種遞送方式 = **約 4,500 種灌注食物**

---

## 灌注台合成配方

```
┌───┬───┬───┐
│   │金錠│   │
├───┼───┼───┤
│玻璃│鑽石│玻璃│
│瓶  │   │瓶  │
├───┼───┼───┤
│黑曜│黑曜│黑曜│
│石  │石  │石  │
└───┴───┴───┘
```

獲得**金錠**後，配方會在配方書中自動解鎖。

---

## 安裝說明

1. 從 [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases)、[CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions) 或 [Modrinth](https://modrinth.com/mod/ediblepotions) 下載對應平台的 `.jar`
2. 放入 `mods` 資料夾
3. 啟動遊戲

### 依賴

| 平台 | 需要安裝 |
|------|---------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## 從原始碼建置

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # 建置雙平台
./gradlew :forge:runData      # 重新生成物品模型（修改配方／物品後）
./gradlew :forge:runClient    # 啟動 Forge 開發客戶端
./gradlew :fabric:runClient   # 啟動 Fabric 開發客戶端
```

需要 **JDK 17**。

---

## 開源協議

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Made with ❤️ by <b>lingyunmo</b> · Built on <a href="https://architectury.dev/">Architectury</a>
</div>
