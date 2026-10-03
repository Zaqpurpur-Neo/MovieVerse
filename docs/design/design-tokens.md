# Design Tokens — Cinema Dynamic Tone (Material 3 Movie Catalog)

Dokumen ini mendefinisikan sistem token desain (**Design Tokens**) untuk aplikasi **MovieVerse** berdasarkan Design System resmi **"Cinema Dynamic Tone"** dari project Stitch *Material 3 Movie Catalog* serta implementasi kode HTML dari 7 layar utama.

> [!NOTE]
> Token yang bersumber langsung dari Design System resmi **Cinema Dynamic Tone** ditandai sebagai `[RESMI / STITCH]`.
> Bagian yang tidak tercantum di spesifikasi resmi namun ditemukan pada kode HTML atau merupakan inferensi/asumsi implementasi ditandai dengan label **`[INFERENSI / TEBAKAN]`**.

---

## 1. Palet Warna (Color Palette)

Tema utama mengadopsi **Material Design 3 (M3) Dark Mode** dengan aksen *Lavender/Violet* (*Primary*) dan *Soft Rose* (*Tertiary*) berlatar kanvas gelap sinematik.

### 1.1. Warna Utama & Kontainer (Primary, Secondary, Tertiary)

| Token Role | Hex Code | Deskripsi & Penggunaan | Sumber |
|---|---|---|:---:|
| `primary` | `#E9DDFF` | Warna aksen utama, tombol utama, indikator aktif navigasi, teks sorotan | `[RESMI / STITCH]` |
| `on-primary` | `#37265E` | Teks/ikon di atas elemen `primary` | `[RESMI / STITCH]` |
| `primary-container` | `#D0BCFF` | Kontainer tombol utama, chip aktif terpilih, pill badge aktif | `[RESMI / STITCH]` |
| `on-primary-container` | `#594983` | Teks/ikon di atas `primary-container` | `[RESMI / STITCH]` |
| `inverse-primary` | `#665590` | Versi kontras inverse dari warna primary | `[RESMI / STITCH]` |
| `primary-fixed` | `#E9DDFF` | Warna primary tetap tanpa modulasi tema | `[RESMI / STITCH]` |
| `primary-fixed-dim` | `#D0BCFF` | Varian redup dari primary fixed | `[RESMI / STITCH]` |
| `on-primary-fixed` | `#210F48` | Teks di atas primary fixed | `[RESMI / STITCH]` |
| `on-primary-fixed-variant` | `#4D3D76` | Teks sekunder di atas primary fixed | `[RESMI / STITCH]` |
| `secondary` | `#CCC2DC` | Aksen sekunder (slate-violet), chip filter inaktif, subtitle | `[RESMI / STITCH]` |
| `on-secondary` | `#332D41` | Teks/ikon di atas warna `secondary` | `[RESMI / STITCH]` |
| `secondary-container` | `#4A4359` | Kontainer sekunder, elevated button background | `[RESMI / STITCH]` |
| `on-secondary-container` | `#BAB1CA` | Teks/ikon di atas `secondary-container` | `[RESMI / STITCH]` |
| `secondary-fixed` | `#E9DEF9` | Varian fixed sekunder | `[RESMI / STITCH]` |
| `secondary-fixed-dim` | `#CCC2DC` | Varian fixed redup sekunder | `[RESMI / STITCH]` |
| `on-secondary-fixed` | `#1E182B` | Teks di atas secondary fixed | `[RESMI / STITCH]` |
| `on-secondary-fixed-variant` | `#4A4359` | Teks pendukung di atas secondary fixed | `[RESMI / STITCH]` |
| `tertiary` | `#FFD9E3` | Aksen crimson/rose untuk rating film, bookmark, status favorit | `[RESMI / STITCH]` |
| `on-tertiary` | `#492532` | Teks/ikon di atas elemen `tertiary` | `[RESMI / STITCH]` |
| `tertiary-container` | `#EFB8C8` | Lencana rating bintang, tag promosi khusus | `[RESMI / STITCH]` |
| `on-tertiary-container` | `#704654` | Teks rating di atas `tertiary-container` | `[RESMI / STITCH]` |
| `tertiary-fixed` | `#FFD9E3` | Warna tertiary tetap | `[RESMI / STITCH]` |
| `tertiary-fixed-dim` | `#EFB8C8` | Varian fixed redup tertiary | `[RESMI / STITCH]` |
| `on-tertiary-fixed` | `#31111D` | Teks di atas tertiary fixed | `[RESMI / STITCH]` |
| `on-tertiary-fixed-variant` | `#633B48` | Teks pendukung di atas tertiary fixed | `[RESMI / STITCH]` |

### 1.2. Surface & Background (Tonal Elevation)

| Token Role | Hex Code | Deskripsi & Penggunaan | Sumber |
|---|---|---|:---:|
| `background` | `#141218` | Latar belakang kanvas aplikasi | `[RESMI / STITCH]` |
| `on-background` | `#E7E0E9` | Teks utama pada background kanvas | `[RESMI / STITCH]` |
| `surface` | `#141218` | Permukaan dasar komponen / Level 0 | `[RESMI / STITCH]` |
| `surface-dim` | `#141218` | Permukaan redup | `[RESMI / STITCH]` |
| `surface-bright` | `#3B383F` | Permukaan kontras terang pada dark mode | `[RESMI / STITCH]` |
| `surface-container-lowest` | `#0F0D13` | Tingkat elevasi terendah (inset cards/well) | `[RESMI / STITCH]` |
| `surface-container-low` | `#1D1B21` | Kartu daftar film, item list standard | `[RESMI / STITCH]` |
| `surface-container` | `#211F25` | Kartu konten, review box standard | `[RESMI / STITCH]` |
| `surface-container-high` | `#2B292F` | Kolom pencarian (Search Bar), bottom sheet | `[RESMI / STITCH]` |
| `surface-container-highest` | `#36343A` | Modal dialog, floating navigation rail/bar | `[RESMI / STITCH]` |
| `on-surface` | `#E7E0E9` | Teks dan ikon utama (High Contrast) | `[RESMI / STITCH]` |
| `on-surface-variant` | `#CAC4D0` | Teks sekunder, label placeholder, ikon pembantu | `[RESMI / STITCH]` |
| `surface-variant` | `#36343A` | Pemisah dan outline non-aktif | `[RESMI / STITCH]` |
| `surface-tint` | `#D0BCFF` | Warna tint elevasi M3 | `[RESMI / STITCH]` |
| `inverse-surface` | `#E7E0E9` | Permukaan kontras terbalik (misal: Snackbar) | `[RESMI / STITCH]` |
| `inverse-on-surface` | `#322F36` | Teks di atas inverse surface | `[RESMI / STITCH]` |

### 1.3. Outline & Status Feedback (Error, Stroke)

| Token Role | Hex Code | Deskripsi & Penggunaan | Sumber |
|---|---|---|:---:|
| `outline` | `#948F9A` | Garis tepi aktif, batas input field | `[RESMI / STITCH]` |
| `outline-variant` | `#49454F` | Garis pembatas halus (divider), outline inaktif | `[RESMI / STITCH]` |
| `error` | `#FFB4AB` | Indikator kesalahan, tombol hapus/batal | `[RESMI / STITCH]` |
| `on-error` | `#690005` | Teks di atas warna error | `[RESMI / STITCH]` |
| `error-container` | `#93000A` | Wadah pesan kesalahan / peringatan | `[RESMI / STITCH]` |
| `on-error-container` | `#FFDAD6` | Teks di atas wadah pesan error | `[RESMI / STITCH]` |

### 1.4. Varian Warna pada Export HTML *(Perbedaan Temuan)*

> [!WARNING]
> Pada konfigurasi Tailwind CSS di dalam berkas HTML hasil ekspor Stitch, ditemukan beberapa pergeseran nilai hex mikro (kemungkinan variasi palette generator otomatis):
> - `[INFERENSI / TEBAKAN]` HTML Primary: `#ECD7FF` (Resmi: `#E9DDFF`)
> - `[INFERENSI / TEBAKAN]` HTML Surface: `#131315` (Resmi: `#141218`)
> - `[INFERENSI / TEBAKAN]` HTML Surface Container High: `#2A2A2C` (Resmi: `#2B292F`)
> - `[INFERENSI / TEBAKAN]` HTML Secondary: `#DDB8FF` (Resmi: `#CCC2DC`)
> 
> **Rekomendasi untuk Android**: Gunakan nilai resmi dari Design System (`Cinema Dynamic Tone`) pada subbab 1.1 - 1.3.

---

## 2. Tipografi (Typography — Roboto Flex)

Keluarga font resmi adalah **Roboto Flex**. Skala tipografi dirancang mengikuti hierarki **Material Design 3 Type Scale**.

### 2.1. Skala Tipografi Resmi

| Role Tipografi | Ukuran (Size) | Bobot (Weight) | Line Height | Letter Spacing | Sumber |
|---|---|---|---|---|:---:|
| `display-lg` | `57px` (`57sp`) | 400 (Regular) | `64px` | `-0.25px` | `[RESMI / STITCH]` |
| `display-lg-mobile` | `36px` (`36sp`) | 400 (Regular) | `44px` | `0px` | `[RESMI / STITCH]` |
| `headline-md` | `28px` (`28sp`) | 400 (Regular) | `36px` | `0px` | `[RESMI / STITCH]` |
| `title-lg` | `22px` (`22sp`) | 500 (Medium) | `28px` | `0px` | `[RESMI / STITCH]` |
| `title-md` | `16px` (`16sp`) | 500 (Medium) | `24px` | `0.15px` | `[RESMI / STITCH]` |
| `body-lg` | `16px` (`16sp`) | 400 (Regular) | `24px` | `0.5px` | `[RESMI / STITCH]` |
| `body-md` | `14px` (`14sp`) | 400 (Regular) | `20px` | `0.25px` | `[RESMI / STITCH]` |
| `label-lg` | `14px` (`14sp`) | 500 (Medium) | `20px` | `0.1px` | `[RESMI / STITCH]` |
| `label-md` | `12px` (`12sp`) | 500 (Medium) | `16px` | `0.5px` | `[RESMI / STITCH]` |
| `label-sm` | `11px` (`11sp`) | 500 (Medium) | `16px` | `0.5px` | `[RESMI / STITCH]` |

### 2.2. Tipografi Tambahan & Penyesuaian Bobot yang Digunakan di HTML

> [!NOTE]
> Pada kode HTML ketujuh layar, terdapat peranan dan bobot tambahan yang sering digunakan untuk komponen spesifik:

| Role / Kelas | Ukuran | Bobot | Penggunaan Komponen | Status Sumber |
|---|---|---|---|:---:|
| `font-headline-lg` | `28px` / `30px` | 600 (Semi-bold) | Hero Title & Banner Utama | `[INFERENSI / TEBAKAN]` (Variasi HTML) |
| `font-body-sm` | `12px` (`12sp`) | 400 (Regular) | Metadata durasi, tanggal rilis, sinopsis pendek | `[INFERENSI / TEBAKAN]` (Tidak ada di spec resmi) |
| `text-[10px]` | `10px` (`10sp`) | 500 / 600 | Tag genre micro, pill bookmark kecil | `[INFERENSI / TEBAKAN]` (Arbitrary HTML) |
| `text-[18px]` | `18px` (`18sp`) | 600 (Semi-bold) | Judul kartu poster film medium | `[INFERENSI / TEBAKAN]` (Arbitrary HTML) |
| `text-[20px]` | `20px` (`20sp`) | 600 (Semi-bold) | Header section (Trending, Rekomendasi) | `[INFERENSI / TEBAKAN]` (Arbitrary HTML) |
| `text-[24px]` | `24px` (`24sp`) | 600 / 700 | Judul film di layar detail | `[INFERENSI / TEBAKAN]` (Arbitrary HTML) |
| `Material Symbols` | `20px` / `24px` | 400 | Ukuran Ikon Navigasi & Aksi | `[INFERENSI / TEBAKAN]` (Spesifikasi Ikon) |

---

## 3. Bentuk Sudut (Corner Radius / Shape)

Menggunakan filosofi kelengkungan dinamis Material 3 (*Rounded Pills* & *Organic Shapes*).

### 3.1. Skala Radius Resmi

| Token | Nilai Rem | Nilai Piksel / DP | Penggunaan Komponen | Sumber |
|---|---|---|---|:---:|
| `rounded-sm` | `0.25rem` | `4px` (`4dp`) | Tooltip kecil, indikator mikro | `[RESMI / STITCH]` |
| `rounded` (`DEFAULT`) | `0.5rem` | `8px` (`8dp`) | Thumbnail kecil, segmented control item | `[RESMI / STITCH]` |
| `rounded-md` | `0.75rem` | `12px` (`12dp`) | Kartu film horizontal, input container | `[RESMI / STITCH]` |
| `rounded-lg` | `1.0rem` | `16px` (`16dp`) | Poster card standar (2:3 ratio), pop-up dialog | `[RESMI / STITCH]` |
| `rounded-xl` | `1.5rem` | `24px` (`24dp`) | Featured banner poster, filter drawer | `[RESMI / STITCH]` |
| `rounded-full` | `9999px` | `Pill Shape` | Filter chip, button CTA, avatar, rating badge | `[RESMI / STITCH]` |

### 3.2. Radius Khusus Komponen *(Berdasarkan HTML)*

| Komponen | Nilai Radius | Keterangan | Status Sumber |
|---|---|---|:---:|
| **Search Anchor Bar** | `28px` (`rounded-3xl`) | Input pencarian di header | `[INFERENSI / TEBAKAN]` |
| **Modal Bottom Sheet** | Top-left & Top-right `28px` | `rounded-t-3xl` | `[INFERENSI / TEBAKAN]` |
| **Poster Card Overlay** | Bottom `16px` / `24px` | `rounded-b-xl` | `[INFERENSI / TEBAKAN]` |

---

## 4. Jarak & Spasi (Spacing, Margins, Gutters, Gaps)

Sistem spasi berpegang pada ritme **8-point grid** dengan sub-kelipatan **4-point micro-grid**.

### 4.1. Skala Spasi Resmi

| Token Spacing | Nilai Rem | Nilai Piksel / DP | Penerapan Komponen | Sumber |
|---|---|---|---|:---:|
| `space-xs` | `0.25rem` | `4px` (`4dp`) | Jarak antara ikon dan teks dalam rating pill | `[RESMI / STITCH]` |
| `space-sm` | `0.5rem` | `8px` (`8dp`) | Jarak antar elemen dalam list / tag genre | `[RESMI / STITCH]` |
| `space-md` | `1.0rem` | `16px` (`16dp`) | Padding dalam kartu (card body padding) | `[RESMI / STITCH]` |
| `space-lg` | `1.5rem` | `24px` (`24dp`) | Jarak vertikal antar section konten | `[RESMI / STITCH]` |
| `space-xl` | `2.0rem` | `32px` (`32dp`) | Jarak vertikal hero header ke section pertama | `[RESMI / STITCH]` |
| `gutter` | `1.0rem` | `16px` (`16dp`) | Jarak antar kolom pada grid tablet/desktop | `[RESMI / STITCH]` |
| `gutter-mobile` | `0.75rem` | `12px` (`12dp`) | Jarak antar poster film pada 2-column grid mobile | `[RESMI / STITCH]` |
| `margin` | `1.5rem` | `24px` (`24dp`) | Margin luar layar pada mode tablet | `[RESMI / STITCH]` |
| `margin-mobile` | `1.0rem` | `16px` (`16dp`) | Margin luar layar kiri-kanan pada mobile | `[RESMI / STITCH]` |

### 4.2. Spasi Mikro & Arbitrary yang Digunakan di HTML

> [!NOTE]
> Pada ketujuh berkas layar HTML, ditemukan utilisasi kelas Tailwind mikro untuk penyesuaian detail layout:

| Kelas Utilitas HTML | Nilai Piksel / DP | Penggunaan Utama | Status Sumber |
|---|---|---|:---:|
| `py-0.5` / `my-0.5` | `2px` (`2dp`) | Padding vertikal rating badge / micro offset | `[INFERENSI / TEBAKAN]` |
| `gap-1` / `p-1` | `4px` (`4dp`) | Gap antar icon dan teks rating | `[INFERENSI / TEBAKAN]` |
| `py-1.5` / `px-1.5` | `6px` (`6dp`) | Padding dalam chip genre / tag status | `[INFERENSI / TEBAKAN]` |
| `gap-1.5` | `6px` (`6dp`) | Jarak antar item badge | `[INFERENSI / TEBAKAN]` |
| `px-3` / `py-2` | `12px` / `8px` | Padding horizontal tombol filled/tonal | `[INFERENSI / TEBAKAN]` |
| `px-4` / `py-3` | `16px` / `12px` | Padding horizontal list item reviewer & review card | `[INFERENSI / TEBAKAN]` |
| `pt-safe` | `44px` - `48px` | Padding atas untuk status bar / notch perangkat mobile | `[INFERENSI / TEBAKAN]` |
| `pb-20` / `pb-24` | `80px` - `96px` | Padding bawah layar agar konten tidak tertutup Bottom Navigation Bar | `[INFERENSI / TEBAKAN]` |

---

## 5. Ringkasan & Panduan Konversi ke Android (Compose)

Untuk implementasi Jetpack Compose di `MovieVerse`:

1. **Colors**: Buat `Color.kt` dan `Theme.kt` dengan `darkColorScheme()` menggunakan token resmi M3 di Bagian 1.
2. **Typography**: Definisikan `Typography.kt` menggunakan font family `RobotoFlex` dengan hierarki M3 di Bagian 2.
3. **Shapes**: Definisikan `Shapes.kt` dengan `CornerBasedShape` (`RoundedCornerShape(8.dp)`, `12.dp`, `16.dp`, `24.dp`, `28.dp`, dan `CircleShape`).
4. **Dimens**: Definisikan `Dimens.kt` untuk nilai spasi `4.dp`, `8.dp`, `12.dp`, `16.dp`, `24.dp`, `32.dp`.
