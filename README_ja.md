<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 **日本語** · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 [Español](README_es.md) · 🇫🇷 [Français](README_fr.md) · 🇲🇾 [Bahasa Melayu](README_ms.md)

# 🍎 Edible Potions

**食べ物に魔法を注入しよう — リンゴを食べて、即座に暗視効果を獲得！**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## 概要

**Edible Potions**は、Minecraftに**注入台（Infusion Table）** という新しい作業台を追加します。この作業台では、ポーション効果を食べ物に直接注入できます。戦闘中に慌ててボトルを探す必要はもうありません — 俊敏のリンゴ、力のステーキ、治癒のパンを食べれば、対応するポーション効果を即座に得られます。

Minecraft **1.20.1** の **Forge** と **Fabric** の両方に完全対応しており、[Architectury](https://architectury.dev/) マルチローダーフレームワークで構築されているため、両プラットフォームで機能が完全に一致します。

---

## 機能

### 🔧 注入台 (Infusion Table)
金インゴット、ダイヤモンド、ガラス瓶、黒曜石で作成する新しい作業台です。左スロットに食べ物を、中央スロットにポーション（通常/スプラッシュ/残留）を入れて、注入が完了するのを待ちます。

### 🧪 3つの効果付与方式
| 方式 | 使用するポーション | 効果 |
|----------|--------------|---------------|
| **通常** | `minecraft:potion` | 食べたプレイヤーにのみ効果を付与 |
| **スプラッシュ** | `minecraft:splash_potion` | 周囲 **3ブロック** 以内の全エンティティに効果が拡散 |
| **残留** | `minecraft:lingering_potion` | 食べた場所に **エリアエフェクトクラウド** を生成 |

3つのバリエーションはクリエイティブタブで別々のアイテムとして表示され、名前で明確に区別されます
（例：*暗視のリンゴ*、*スプラッシュ暗視のリンゴ*、*残留暗視のリンゴ*）。

### ⚡ レッドストーン制御
注入台にレッドストーン信号を入力すると、注入プロセスが**一時停止**します。信号を解除すると再開されます。
コンパレーター出力（進行度に応じて0–15）と組み合わせることで、完全自動化された生産ラインを構築できます。

### 🔄 ホッパー自動化
| 面 | 動作 |
|------|------|
| **上** | **食べ物**（食用アイテム）を投入 |
| **側面** | **ポーション**（通常/スプラッシュ/残留）を投入 |
| **下** | **出力**（注入済み食品）と**空のガラス瓶**を抽出 |

注入台は `WorldlyContainer` インターフェースを実装しており、バニラのホッパー、MODのパイプ、`IItemHandler` / `SidedInventory` APIを準拠するあらゆる物流システムと互換性があります。

### 🎨 ビジュアル & オーディオ
- **IC2スタイルのテクスチャ** — ダークな機械筐体にオレンジのエネルギーアクセントとリベットのディテール
- **食べた時のカラー粒子** — ポーションの色に合わせたダスト粒子がプレイヤーから飛び散る
- **クラフト完了音** — 注入完了時に醸造台の音を再生
- **エンチャントの輝き** — すべての注入済み食品アイテムにエンチャントグリント
- **動的なアイテム名** — 例: "俊敏 II 焼き豚"、"スプラッシュ治癒の金のリンゴ"

### 💀 死亡時の効果保持 *(v1.0.2)*
注入済み食品のアクティブなポーション効果は**死亡しても保持**されます。
リスポーン後、効果は最大 **3分間** 復元されるため、苦労して集めたバフを失わず、かつ過剰にもなりません。

### 🌐 クロスプラットフォーム (Forge & Fabric)
すべてのゲームロジックは共有の `common` モジュールに存在します。プラットフォーム固有のコードは、ブートストラップクラス、データ生成、Mixin登録に限定されています。Architecturyバイトコードトランスフォーマーがビルド時に残りを処理します。

---

## 対応食品

**40種類のバニラ食品**に対応（ケーキ、ミルク入りバケツを除く）:

`リンゴ` · `金のリンゴ` · `エンチャントされた金のリンゴ` · `スイカの薄切り` · `スイートベリー` ·
`グロウベリー` · `コーラスフルーツ` · `ニンジン` · `金のニンジン` · `ジャガイモ` · `ベイクドポテト` ·
`毒のジャガイモ` · `ビートルート` · `乾燥した昆布` · `生の牛肉` · `ステーキ` · `生の豚肉` ·
`焼き豚` · `生の羊肉` · `焼き羊肉` · `生の鶏肉` · `焼き鳥` · `生の兎肉` · `焼き兎肉` ·
`腐った肉` · `タラ` · `焼きタラ` · `サケ` · `焼きサケ` · `熱帯魚` · `フグ` ·
`パン` · `クッキー` · `パンプキンパイ` · `キノコシチュー` · `ビートルートスープ` ·
`ウサギシチュー` · `怪しげなシチュー` · `ハチミツ入りの瓶` · `クモの目`

× すべての有効なポーション効果 × 3つの効果付与方式 = **約4,500種類の注入済み食品**

---

## 注入台のレシピ

```
┌───┬───┬───┐
│   │ G │   │    G = 金インゴット
├───┼───┼───┤    B = ガラス瓶
│ B │ D │ B │    D = ダイヤモンド
├───┼───┼───┤    O = 黒曜石
│ O │ O │ O │
└───┴───┴───┘
```

**金インゴット**を入手すると、レシピがレシピブックに自動的に解放されます。

---

## インストール方法

1. [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases)、[CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)、または [Modrinth](https://modrinth.com/mod/ediblepotions)からプラットフォームに対応した `.jar` をダウンロード
2. `mods` フォルダに配置
3. ゲームを起動

### 依存関係

| プラットフォーム | 必要なもの |
|------|---------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## ソースからビルド

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # 両プラットフォームをビルド
./gradlew :forge:runData      # アイテムモデルを再生成（レシピ/アイテム変更後）
./gradlew :forge:runClient    # Forge開発クライアントを起動
./gradlew :fabric:runClient   # Fabric開発クライアントを起動
```

**JDK 17** が必要です。

---

## ライセンス

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Made with ❤️ by <b>lingyunmo</b> · Built on <a href="https://architectury.dev/">Architectury</a>
</div>
