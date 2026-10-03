# Design Tokens — Cinema Dynamic Tone

Dokumen ini mendokumentasikan spesifikasi token desain untuk **MovieVerse**, yang diadaptasi dari design system **Cinema Dynamic Tone** (Material Design 3 Dark Theme) dan implementasi HTML dari 9 layar Stitch.

> **Catatan Sumber & Label:**
> - Bagian bertanda **`[Sumber: Design System Stitch]`** berasal langsung dari metadata *Cinema Dynamic Tone* (`designMd` & `designTheme`).
> - Bagian bertanda **`[Sumber: HTML Layar]`** berasal dari analisis kode HTML 9 layar yang diekspor.
> - Bagian bertanda **`[Asumsi / Ekstrapolasi]`** adalah nilai pelengkap standar Material 3 yang tidak didefinisikan secara eksplisit di sumber Stitch.
> - **Tidak ada kode Kotlin** dalam dokumen ini.

---

## 1. Palet Warna (Color Palette & Roles M3)

Palet mengadopsi tema **Material Design 3 Dark Mode (Cinema Dark)** dengan warna aksen lavender/violet sebagai identitas visual utama, neutral dark surface untuk kedalaman sinematik, serta rose/tertiary untuk rating dan kurasi film.

### 1.1 Primary & Secondary
| Peran M3 | Nilai Hex | Sumber | Penggunaan & Deskripsi |
|---|---|---|---|
| `primary` | `#E9DDFF` / `#D0BCFF` | `[Sumber: Design System Stitch]` | Aksen tombol utama, active pill indicator, highlight teks penting |
| `onPrimary` | `#37265E` (alt: `#381E72` / `#29074A`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Teks / ikon di atas elemen berlatar `primary` |
| `primaryContainer` | `#D0BCFF` (alt: `#594983`) | `[Sumber: Design System Stitch]` | Kontainer aktif sekunder, badge terpilih |
| `onPrimaryContainer` | `#594983` (alt: `#E9DDFF`) | `[Sumber: Design System Stitch]` | Teks / ikon di atas `primaryContainer` |
| `inversePrimary` | `#665590` | `[Sumber: Design System Stitch]` | Aksen primary pada background terang |
| `secondary` | `#CCC2DC` | `[Sumber: Design System Stitch]` | Filter sekunder, chip pasif, metadata chip |
| `onSecondary` | `#332d41` | `[Sumber: Design System Stitch]` | Teks / ikon di atas elemen berlatar `secondary` |
| `secondaryContainer` | `#4A4359` (alt: `#62259B`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Indikator tab aktif navigasi bawah, container chip |
| `onSecondaryContainer`| `#BAB1CA` (alt: `#D1A1FF`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Teks / ikon di atas `secondaryContainer` |

### 1.2 Tertiary & Accent (Ratings & Curations)
| Peran M3 | Nilai Hex | Sumber | Penggunaan & Deskripsi |
|---|---|---|---|
| `tertiary` | `#FFD9E3` / `#EFB8C8` | `[Sumber: Design System Stitch]` | Badge rating bintang, bookmark, ulasan pilihan |
| `onTertiary` | `#492532` | `[Sumber: Design System Stitch]` | Teks / ikon di atas elemen berlatar `tertiary` |
| `tertiaryContainer` | `#EFB8C8` (alt: `#704654`) | `[Sumber: Design System Stitch]` | Latar belakang badge rating tinggi atau ulasan spesial |
| `onTertiaryContainer` | `#704654` (alt: `#FFD9E3`) | `[Sumber: Design System Stitch]` | Teks / ikon di atas `tertiaryContainer` |

### 1.3 Surface, Container & Background
| Peran M3 | Nilai Hex | Sumber | Tingkat Elevasi / Karakteristik |
|---|---|---|---|
| `background` | `#141218` | `[Sumber: Design System Stitch]` | Canvas utama dasar aplikasi |
| `onBackground` | `#E7E0E9` | `[Sumber: Design System Stitch]` | Teks & ikon utama di atas background |
| `surface` | `#141218` (alt: `#131315`) | `[Sumber: Design System Stitch]` | Latar belakang dasar screen dan app bar |
| `surfaceDim` | `#141218` | `[Sumber: Design System Stitch]` | Latar belakang redup |
| `surfaceBright` | `#3B383F` | `[Sumber: Design System Stitch]` | Permukaan yang lebih terang untuk highlight baris |
| `surfaceContainerLowest` | `#0F0D13` (alt: `#0E0E10`) | `[Sumber: Design System Stitch]` | Kontras terdalam (overlay rating poster) |
| `surfaceContainerLow` | `#1D1B21` (alt: `#1B1B1D`) | `[Sumber: Design System Stitch]` | Elevasi 1: Card poster resting, search box background |
| `surfaceContainer` | `#211F25` (alt: `#201F21`) | `[Sumber: Design System Stitch]` | Elevasi 2: Standard card item, filter chip default |
| `surfaceContainerHigh` | `#2B292F` (alt: `#2A2A2C`) | `[Sumber: Design System Stitch]` | Elevasi 3: Header sticky, bottom bar container, rating badge container |
| `surfaceContainerHighest` | `#36343A` (alt: `#353437`) | `[Sumber: Design System Stitch]` | Elevasi 4: Modal bottom sheet (Tulis/Edit Ulasan), context dialog |
| `onSurface` | `#E7E0E9` (alt: `#E5E1E4`) | `[Sumber: Design System Stitch]` | Teks judul, label utama, ikon utama |
| `onSurfaceVariant` | `#CAC4D0` (alt: `#CDC3D0`) | `[Sumber: Design System Stitch]` | Subtitle, metadata tahun/genre, placeholder input |
| `surfaceVariant` | `#36343A` (alt: `#49454F`) | `[Sumber: Design System Stitch]` | Garis batas komponen pasif, chip tidak aktif |
| `surfaceTint` | `#D0BCFF` | `[Sumber: Design System Stitch]` | Tint overlay elevasi khas M3 |
| `inverseSurface` | `#E7E0E9` | `[Sumber: Design System Stitch]` | Snackbar / popup terbalik kontras tinggi |
| `inverseOnSurface` | `#322F36` | `[Sumber: Design System Stitch]` | Teks di atas `inverseSurface` |

### 1.4 Outline & Feedback (Error)
| Peran M3 | Nilai Hex | Sumber | Penggunaan & Deskripsi |
|---|---|---|---|
| `outline` | `#948F9A` (alt: `#968E9A`) | `[Sumber: Design System Stitch]` | Border input form, divider tegas |
| `outlineVariant` | `#49454F` (alt: `#4A454F`) | `[Sumber: Design System Stitch]` | Divider halus, outline filter chip unselected |
| `error` | `#FFB4AB` | `[Sumber: Design System Stitch]` | Status error, tombol hapus ulasan |
| `onError` | `#690005` | `[Sumber: Design System Stitch]` | Teks / ikon di atas elemen berlatar `error` |
| `errorContainer` | `#93000A` | `[Sumber: Design System Stitch]` | Container pesan peringatan / error banner |
| `onErrorContainer` | `#FFDAD6` | `[Sumber: Design System Stitch]` | Teks di atas `errorContainer` |

### 1.5 Fixed Roles (Tonal Fixed Elements)
| Peran M3 | Nilai Hex | Sumber | Penggunaan |
|---|---|---|---|
| `primaryFixed` | `#E9DDFF` | `[Sumber: Design System Stitch]` | Elemen tetap yang membutuhkan aksen primer konstan |
| `primaryFixedDim` | `#D0BCFF` | `[Sumber: Design System Stitch]` | Versi redup dari `primaryFixed` |
| `onPrimaryFixed` | `#210F48` | `[Sumber: Design System Stitch]` | Teks di atas `primaryFixed` |
| `onPrimaryFixedVariant` | `#4D3D76` | `[Sumber: Design System Stitch]` | Teks pendukung di atas `primaryFixed` |
| `secondaryFixed` | `#E9DEF9` | `[Sumber: Design System Stitch]` | Elemen filter chip tetap |
| `secondaryFixedDim` | `#CCC2DC` | `[Sumber: Design System Stitch]` | Versi redup `secondaryFixed` |
| `onSecondaryFixed` | `#1E182B` | `[Sumber: Design System Stitch]` | Teks di atas `secondaryFixed` |
| `onSecondaryFixedVariant` | `#4A4359` | `[Sumber: Design System Stitch]` | Teks pendukung di atas `secondaryFixed` |
| `tertiaryFixed` | `#FFD9E3` | `[Sumber: Design System Stitch]` | Badge rating fixed |
| `tertiaryFixedDim` | `#EFB8C8` | `[Sumber: Design System Stitch]` | Versi redup `tertiaryFixed` |
| `onTertiaryFixed` | `#31111D` | `[Sumber: Design System Stitch]` | Teks di atas `tertiaryFixed` |
| `onTertiaryFixedVariant` | `#633B48` | `[Sumber: Design System Stitch]` | Teks pendukung di atas `tertiaryFixed` |

---

## 2. Skala Tipografi (Typography Scale)

- **Keluarga Font Utama**: `Roboto Flex` `[Sumber: Design System Stitch]`
- *Catatan Implementasi Web/HTML*: Pada ekspor HTML Stitch, font fallback web menggunakan `Inter`, namun spesifikasi desain M3 mewajibkan `Roboto Flex`.

| Kategori M3 | Ukuran (Size) | Line Height | Bobot (Weight) | Letter Spacing | Sumber | Kasus Penggunaan Utama |
|---|---|---|---|---|---|---|
| `displayLarge` | 57px | 64px | Regular (400) | -0.25px | `[Sumber: Design System Stitch]` | Hero billboard title (tablet / desktop) |
| `displayMedium` | 45px | 52px | Regular (400) | 0.0px | `[Asumsi / Ekstrapolasi]` | Header promosi besar |
| `displaySmall` (Mobile Hero) | 36px | 44px | Regular (400) / Bold (700) | 0.0px (`-0.02em`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Judul layar utama (e.g. CineVerse Splash / Hero) |
| `headlineLarge` | 32px (alt: 28px) | 40px (alt: 36px) | SemiBold (600) | 0.0px | `[Sumber: HTML Layar]` | Judul section utama pada overlay |
| `headlineMedium` | 28px (alt: 22px) | 36px (alt: 28px) | Regular (400) / Bold (700) | 0.0px | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Judul film unggulan (Hero Banner Movie Title), logo brand MovieVerse |
| `headlineSmall` | 24px | 32px | Regular (400) | 0.0px | `[Asumsi / Ekstrapolasi]` | Judul modal dialog |
| `titleLarge` | 22px (alt: 18px) | 28px (alt: 24px) | Medium (500) / SemiBold (600) | 0.0px | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Judul section ("Film Populer", "Rilis Terbaru", "Koleksi Saya") |
| `titleMedium` | 16px | 24px (alt: 22px) | Medium (500) / SemiBold (600) | +0.15px | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Judul kartu film (Movie Card Title), judul ulasan |
| `titleSmall` | 14px | 20px | Medium (500) | +0.10px | `[Asumsi / Ekstrapolasi]` | Sub-judul kartu daftar ringkas |
| `bodyLarge` | 16px | 24px | Regular (400) | +0.50px | `[Sumber: Design System Stitch]` | Sinopsis film lengkap, isi teks ulasan panjang |
| `bodyMedium` | 14px | 20px | Regular (400) | +0.25px | `[Sumber: Design System Stitch]` | Teks ulasan standar, bio reviewer, input form text |
| `bodySmall` | 12px | 16px | Regular (400) | +0.40px | `[Sumber: HTML Layar]` | Metadata pendukung (Tahun rilis • Genre • Durasi) |
| `labelLarge` | 14px | 20px | Medium (500) / SemiBold (600) | +0.10px | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Teks tombol ("Tonton Trailer", "Tulis Ulasan", "Simpan") |
| `labelMedium` | 12px | 16px | Medium (500) / SemiBold (600) | +0.50px (`+0.02em`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Chip kategori filter ("Semua", "Aksi", "Sci-Fi"), angka rating bintang |
| `labelSmall` | 11px | 16px (alt: 14px) | Medium (500) / SemiBold (600) | +0.50px (`+0.03em`) | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Label navigasi bawah (Jelajah, Kategori, Jurnal, Akun), badge status |

---

## 3. Bentuk Sudut (Shape Corners & Corner Radii)

Skema bentuk mengacu pada hierarki sudut Material 3 berbasis kurva organik dan pill bulat untuk kenyamanan interaksi sentuhan.

| Token M3 | Nilai Radius (dp / px) | Nilai Tailwind | Sumber | Kasus Penggunaan Komponen |
|---|---|---|---|---|
| **Extra Small** | `4dp` (0.25rem) | `rounded-sm` / `rounded` | `[Sumber: Design System Stitch]` | Mini badge rating poster (`px-1.5 py-0.5 rounded`), tag status kecil |
| **Small** | `8dp` (0.5rem) | `rounded-lg` / `rounded-DEFAULT` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Badge rating di hero banner, thumbnail item baris, tooltip |
| **Medium** | `12dp` (0.75rem) | `rounded-xl` / `rounded-md` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Poster kartu film (2:3 aspect ratio), Search Bar box input, tombol aksi sekunder |
| **Large** | `16dp` (1.0rem) | `rounded-2xl` / `rounded-lg` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Hero Feature Card (Dune 2 Banner), Modal Card Ulasan, dialog box |
| **Extra Large** | `28dp` (1.75rem) | `rounded-3xl` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Top header Modal Bottom Sheet (Tulis Ulasan / Edit Ulasan), Floating Action Button (FAB) |
| **Full (Pill)** | `9999px` (Full Pill) | `rounded-full` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` | Filter chip tombol, Active navigation pill indicator (64dp x 32dp), Button utama ("Tonton Trailer"), Avatar profil bulat |

---

## 4. Jarak & Tata Letak (Spacing & Layout Grid)

Sistem layout menggunakan grid modular 8-point baseline dengan micro-increment 4-point.

### 4.1 Spacing Scale (Padding / Margin / Gap)
| Token Token Spacing | Ukuran (dp / px) | Nilai Tailwind | Sumber | Penggunaan |
|---|---|---|---|---|
| `space-none` | `0dp` | `p-0`, `m-0`, `gap-0` | `[Sumber: Standar M3]` | Reset margin/padding |
| `space-xs` | `4dp` (0.25rem) | `space-xs` / `p-1`, `gap-1` | `[Sumber: Design System Stitch]` | Jarak antar ikon dan teks inline, gap badge rating bintang |
| `space-sm` | `8dp` (0.5rem) | `space-sm` / `p-2`, `gap-2` | `[Sumber: Design System Stitch]` | Jarak antar chip filter dalam horizontal scroll, jarak elemen form rapat |
| `space-md` | `16dp` (1.0rem) | `space-md` / `p-4`, `gap-4` | `[Sumber: Design System Stitch]` | Padding dalam kartu film, gap antar grid kolom poster, padding container bottom sheet |
| `space-lg` | `24dp` (1.5rem) | `space-lg` / `p-6`, `gap-6` | `[Sumber: Design System Stitch]` | Jarak vertikal antar section ("Film Populer" ke "Rilis Terbaru"), bottom padding modal |
| `space-xl` | `32dp` (2.0rem) | `space-xl` / `p-8`, `gap-8` | `[Sumber: Design System Stitch]` | Padding hero banner besar, jarak konten akhir sebelum bottom bar |

### 4.2 Grid & Viewport Metrics
| Parameter | Nilai (Mobile) | Nilai (Tablet / Wide) | Sumber |
|---|---|---|---|
| **Screen Margin (Canvas)** | `16dp` (`margin-mobile` / `px-4`) | `24dp` (`margin` / `px-6`) | `[Sumber: Design System Stitch]` |
| **Gutter Grid (Poster)** | `12dp` (`gutter-mobile` / `gap-3`) | `16dp` (`gutter` / `gap-4`) | `[Sumber: Design System Stitch]` |
| **Kolom Grid Poster** | 2 Kolom (`grid-cols-2`) | 3–4 Kolom | `[Sumber: HTML Layar]` & `[Asumsi / Ekstrapolasi]` |
| **Tinggi Top App Bar** | `64dp` (`h-16`) | `64dp` (`h-16`) | `[Sumber: HTML Layar]` |
| **Tinggi Bottom Navigation** | `80dp` (dengan safe-area) | `80dp` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` |
| **Ukuran Indikator Aktif Navigasi** | `56dp x 32dp` / `64dp x 32dp` | `64dp x 32dp` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` |
| **Minimum Touch Target** | `44dp`–`48dp` (`min-h-[44px]` / `w-11 h-11`) | `48dp` | `[Sumber: HTML Layar]` & `[Sumber: Standar M3]` |
| **Aspect Ratio Poster Film** | `2:3` (`aspect-[2/3]`) | `2:3` | `[Sumber: Design System Stitch]` & `[Sumber: HTML Layar]` |
