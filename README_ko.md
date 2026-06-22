<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 **한국어** · 🇪🇸 [Español](README_es.md) · 🇫🇷 [Français](README_fr.md) · 🇲🇾 [Bahasa Melayu](README_ms.md)

# 🍎 Edible Potions

**마법을 음식에 주입하세요 — 사과 하나를 먹고 야간 투시를 즉시 획득!**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## 개요

**Edible Potions**는 **주입대(Infusion Table)** 라는 새로운 작업대를 추가합니다. 이 작업대에서는 포션 효과를 음식에 직접 주입할 수 있습니다. 더 이상 전투 중에 물약병을 찾느라 허둥대지 마세요 — 신속의 사과, 힘의 스테이크, 치유의 빵을 먹으면 즉시 효과를 얻을 수 있습니다.

Minecraft **1.20.1**에서 **Forge**와 **Fabric**을 모두 완벽하게 지원하며, [Architectury](https://architectury.dev/) 멀티로더 프레임워크로 제작되어 두 플랫폼에서 기능이 완전히 동일합니다.

---

## 주요 기능

### 🔧 주입대 (Infusion Table)
금괴, 다이아몬드, 유리병, 흑요석으로 제작하는 새로운 작업대입니다. 왼쪽 슬롯에 음식을, 가운데 슬롯에 포션(일반/투척/잔류)을 넣고 기다리면 주입이 완료됩니다.

### 🧪 세 가지 전달 방식
| 전달 방식 | 사용 포션 | 섭취 효과 |
|----------|---------|---------|
| **일반** | `minecraft:potion` | 먹은 사람에게만 효과 적용 |
| **투척** | `minecraft:splash_potion` | 주변 **3블록** 내 모든 개체에 효과 확산 |
| **잔류** | `minecraft:lingering_potion` | 먹은 위치에 **효과 구름(Area Effect Cloud)** 생성 |

세 가지 변종은 각각 별도의 아이템으로 크리에이티브 탭에 등장하며, 이름으로 명확히 구분됩니다
(예: *야간 투시의 사과*, *투척 야간 투시의 사과*, *잔류 야간 투시의 사과*).

### ⚡ 레드스톤 제어
주입대에 레드스톤 신호를 가하면 주입 과정이 **일시 정지**됩니다. 신호를 제거하면 재개됩니다.
비교기 출력(진행도에 따라 0–15)과 조합하면 완전 자동화된 생산 라인을 구축할 수 있습니다.

### 🔄 호퍼 자동화
| 방향 | 동작 |
|------|------|
| **위쪽** | **음식**(먹을 수 있는 아이템) 투입 |
| **측면** | **포션**(일반/투척/잔류) 투입 |
| **아래쪽** | **결과물**(주입된 음식) 및 **빈 유리병** 추출 |

주입대는 `WorldlyContainer` 인터페이스를 구현하여 바닐라 호퍼, 모드 파이프, 그리고 `IItemHandler` / `SidedInventory` API를 준수하는 모든 물류 시스템과 호환됩니다.

### 🎨 시각 및 음향 효과
- **IC2 스타일 텍스처** — 어두운 기계 케이싱에 주황색 에너지 악센트와 리벳 디테일
- **섭취 시 색상 입자** — 포션 색상에 맞춘 컬러 더스트 입자가 플레이어에게서 분출
- **제작 완료 소리** — 주입 완료 시 양조기 소리 재생
- **마법부여된 빛** — 모든 주입된 음식 아이템에 마법부여 광택 효과
- **동적 아이템 이름** — 예: "신속 II 구운 돼지고기", "투척 치유의 황금 사과"

### 💀 사망 시 효과 보존 *(v1.0.2)*
주입된 음식의 활성 포션 효과는 **사망 후에도 보존**됩니다.
부활 후 효과는 최대 **3분** 동안 복원되므로, 힘들게 얻은 버프를 잃지 않으면서도 지나치게 강력하지 않습니다.

### 🌐 크로스 플랫폼 (Forge & Fabric)
모든 게임 로직은 공유 `common` 모듈에 존재합니다. 플랫폼별 코드는 부트스트랩 클래스, 데이터 생성, Mixin 등록으로 제한됩니다. Architectury 바이트코드 변환기가 빌드 시 나머지를 처리합니다.

---

## 지원하는 음식

**40가지 바닐라 음식**을 지원합니다 (케이크, 우유 양동이 제외):

`사과` · `황금 사과` · `마법부여된 황금 사과` · `수박 조각` · `달콤한 열매` ·
`발광 열매` · `후렴과` · `당근` · `황금 당근` · `감자` · `구운 감자` ·
`독이 있는 감자` · `비트` · `말린 켈프` · `익히지 않은 소고기` · `스테이크` ·
`익히지 않은 돼지고기` · `구운 돼지고기` · `익히지 않은 양고기` · `구운 양고기` ·
`익히지 않은 닭고기` · `구운 닭고기` · `익히지 않은 토끼고기` · `구운 토끼고기` ·
`썩은 살점` · `대구` · `익힌 대구` · `연어` · `익힌 연어` · `열대어` ·
`복어` · `빵` · `쿠키` · `호박 파이` · `버섯 스튜` · `비트 수프` ·
`토끼 스튜` · `수상한 스튜` · `꿀이 든 병` · `거미 눈`

× 모든 유효한 포션 효과 × 3가지 전달 방식 = **약 4,500가지 주입된 음식**

---

## 주입대 제작법

```
┌───┬───┬───┐
│   │ G │   │    G = 금괴
├───┼───┼───┤    B = 유리병
│ B │ D │ B │    D = 다이아몬드
├───┼───┼───┤    O = 흑요석
│ O │ O │ O │
└───┴───┴───┘
```

**금괴**를 획득하면 제작법 책에 자동으로 해금됩니다.

---

## 설치 방법

1. [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions), 또는 [Modrinth](https://modrinth.com/mod/ediblepotions)에서 플랫폼에 맞는 `.jar` 다운로드
2. `mods` 폴더에 넣기
3. 게임 실행

### 의존성

| 플랫폼 | 필요 모드 |
|------|---------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## 소스에서 빌드하기

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # 두 플랫폼 빌드
./gradlew :forge:runData      # 아이템 모델 재생성 (레시피/아이템 변경 후)
./gradlew :forge:runClient    # Forge 개발 클라이언트 실행
./gradlew :fabric:runClient   # Fabric 개발 클라이언트 실행
```

**JDK 17**이 필요합니다.

---

## 라이선스

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Made with ❤️ by <b>lingyunmo</b> · Built on <a href="https://architectury.dev/">Architectury</a>
</div>
