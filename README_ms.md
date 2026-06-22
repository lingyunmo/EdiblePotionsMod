<div align="center">

**Languages:** 🇺🇸 [English](README.md) · 🇨🇳 [简体中文](README_zh.md) · 🇹🇼 [繁體中文](README_zh_tw.md) · 🇯🇵 [日本語](README_ja.md) · 🇰🇷 [한국어](README_ko.md) · 🇪🇸 [Español](README_es.md) · 🇫🇷 [Français](README_fr.md) · 🇲🇾 **Bahasa Melayu**

# 🍎 Edible Potions

**Suntikkan sihir ke dalam makanan anda — makan sebiji epal, dapat Penglihatan Malam serta-merta!**

[![Release](https://img.shields.io/github/v/release/lingyunmo/EdiblePotionsMod?style=flat-square&color=E05D44)](https://github.com/lingyunmo/EdiblePotionsMod/releases)
[![License](https://img.shields.io/github/license/lingyunmo/EdiblePotionsMod?style=flat-square&color=blue)](LICENSE)
[![CurseForge](https://img.shields.io/badge/CurseForge-Available-orange?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/ediblepotions)
[![Modrinth](https://img.shields.io/badge/Modrinth-Available-green?style=flat-square&logo=modrinth)](https://modrinth.com/mod/ediblepotions)

![Icon](common/src/main/resources/assets/ediblepotions/icon.png)

</div>

---

## Gambaran

**Edible Potions** menambah **Meja Infusi** ke Minecraft, sebuah stesen kerja baharu yang menyuntik kesan ramuan terus ke dalam makanan. Tiada lagi meraba-raba mencari botol semasa pertempuran — makan Epal Kepantasan, Stik Kekuatan, atau Roti Penyembuhan dan dapatkan kesan ramuan serta-merta.

Serasi sepenuhnya dengan **Forge** dan **Fabric** pada Minecraft **1.20.1**, dibina dengan rangka kerja pelbagai pemuat [Architectury](https://architectury.dev/) untuk pariti ciri di kedua-dua platform.

---

## Ciri-ciri

### 🔧 Meja Infusi
Blok stesen kerja baharu yang dibuat daripada Emas, Berlian, Botol Kaca, dan Obsidian. Letakkan makanan di slot kiri, sebarang ramuan (biasa / percikan / berlarutan) di tengah, dan tunggu infusi selesai.

### 🧪 Tiga Mod Penyampaian
| Mod | Ramuan Sumber | Kesan Makan |
|----------|--------------|---------------|
| **Biasa** | `minecraft:potion` | Kesan dikenakan pada pemakan sahaja |
| **Percikan** | `minecraft:splash_potion` | Kesan merebak ke semua entiti dalam **3 blok** |
| **Berlarutan** | `minecraft:lingering_potion` | Meninggalkan **Awan Kesan** di kedudukan pemakan |

Ketiga-tiga varian muncul sebagai item berasingan dalam tab kreatif dengan nama yang berbeza
(cth. *Epal Penglihatan Malam*, *Epal Percikan Penglihatan Malam*, *Epal Berlarutan Penglihatan Malam*).

### ⚡ Kawalan Redstone
Memberikan isyarat redstone kepada Meja Infusi **menjeda** proses infusi. Alihkan isyarat untuk menyambung semula.
Digabungkan dengan output pembanding (0–15 berdasarkan kemajuan), anda boleh membina barisan pengeluaran automatik sepenuhnya.

### 🔄 Automasi Corong
| Muka | Tingkah Laku |
|--------|----------|
| **Atas** | Input **makanan** (sebarang item boleh dimakan) |
| **Sisi** | Input **ramuan** (biasa / percikan / berlarutan) |
| **Bawah** | Ekstrak **output** (makanan diinfusi) dan **botol kaca kosong** |

Meja Infusi melaksanakan `WorldlyContainer` — serasi dengan corong vanilla, paip mod, dan sebarang pengangkutan item yang mematuhi API `IItemHandler` / `SidedInventory`.

### 🎨 Penggilapan Visual & Audio
- **Tekstur gaya IC2** — selongsong mesin gelap dengan aksen tenaga oren dan butiran rivet
- **Zarah berwarna** semasa makan — zarah debu berwarna ramuan meletus dari pemain
- **Bunyi menghasilkan** — bunyi tempat pembruan semasa infusi selesai
- **Kilauan terpesona** pada semua item makanan diinfusi
- **Nama item dinamik** — cth. "Daging Babi Masak Kepantasan II", "Epal Emas Percikan Penyembuhan"

### 💀 Kesan Berterusan Selepas Mati *(v1.0.2)*
Kesan ramuan aktif daripada makanan diinfusi **dipelihara selepas kematian**.
Selepas hidup semula, kesan dipulihkan dengan tempoh maksimum **3 minit**,
supaya anda tidak kehilangan buff yang diperoleh dengan susah payah tetapi juga tidak terlalu kuat.

### 🌐 Merentas Platform (Forge & Fabric)
Semua logik permainan terletak dalam modul `common` yang dikongsi. Kod khusus platform terhad kepada kelas bootstrap, penjanaan data, dan pendaftaran Mixin. Pengubah bait kod Architectury mengendalikan selebihnya semasa pembinaan.

---

## Makanan Disokong

Kesemua **40 makanan vanilla** disokong (Kek dan Baldi Susu dikecualikan):

`Epal` · `Epal Emas` · `Epal Emas Tersihir` · `Hirisan Tembikai` · `Beri Manis` ·
`Beri Bercahaya` · `Buah Korus` · `Lobak` · `Lobak Emas` · `Kentang` · `Kentang Bakar` ·
`Kentang Beracun` · `Ubi Bit` · `Rumpai Kering` · `Daging Lembu Mentah` · `Stik` ·
`Daging Babi Mentah` · `Daging Babi Masak` · `Daging Biri-biri Mentah` · `Daging Biri-biri Masak` ·
`Ayam Mentah` · `Ayam Masak` · `Arnab Mentah` · `Arnab Masak` · `Daging Reput` ·
`Ikan Kod Mentah` · `Ikan Kod Masak` · `Salmon Mentah` · `Salmon Masak` · `Ikan Tropika` ·
`Ikan Buntal` · `Roti` · `Biskut` · `Pai Labu` · `Rebus Cendawan` · `Sup Ubi Bit` ·
`Rebus Arnab` · `Rebus Mencurigakan` · `Botol Madu` · `Mata Labah-labah`

× semua kesan ramuan sah × 3 mod penyampaian = **~4,500 makanan diinfusi**

---

## Resipi Meja Infusi

```
┌───┬───┬───┐
│   │ E │   │    E = Jongkong Emas
├───┼───┼───┤    B = Botol Kaca
│ B │ D │ B │    D = Berlian
├───┼───┼───┤    O = Obsidian
│ O │ O │ O │
└───┴───┴───┘
```

Resipi dibuka dalam buku resipi apabila anda memperoleh **Jongkong Emas**.

---

## Pemasangan

1. Muat turun `.jar` untuk platform anda dari [Releases](https://github.com/lingyunmo/EdiblePotionsMod/releases), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ediblepotions), atau [Modrinth](https://modrinth.com/mod/ediblepotions)
2. Letakkan dalam folder `mods` anda
3. Lancarkan permainan

### Kebergantungan

| Platform | Diperlukan |
|----------|----------|
| **Fabric** | [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) |
| **Forge** | [Architectury API](https://modrinth.com/mod/architectury-api) |

---

## Membina dari Sumber

```bash
git clone https://github.com/lingyunmo/EdiblePotionsMod.git
cd EdiblePotionsMod
./gradlew build              # Bina kedua-dua platform
./gradlew :forge:runData      # Jana semula model item (selepas menukar resipi/item)
./gradlew :forge:runClient    # Lancar klien pembangunan Forge
./gradlew :fabric:runClient   # Lancar klien pembangunan Fabric
```

Memerlukan **JDK 17**.

---

## Lesen

[GNU GPL v3](LICENSE) © lingyunmo

---

<div align="center">
Dibuat dengan ❤️ oleh <b>lingyunmo</b> · Dibina dengan <a href="https://architectury.dev/">Architectury</a>
</div>
