# MovieVerse — Master Guidebook

**Project:** MovieVerse — aplikasi Android native (Jetpack Compose) untuk menjelajah film, menyimpan watchlist, dan menulis jurnal ulasan; seluruh metadata film berasal dari TMDB API v3.
**Repo:** `https://github.com/Zaqpurpur-Neo/MovieVerse` (HEAD `14445c4`, branch `main`, sinkron dengan `origin`)
**Compiled:** 2026-10-04 (state reconciled against working tree + `assembleDebug` + installasi nyata ke perangkat)
**Compiled by:** Merpatidove
**Public docs:** `D:\movieverse\README.md` — **saat ini kosong** (lihat §10 Known Issues)
**One-command deploy:** `.\gradlew.bat :app:assembleDebug --console=plain` → `adb install -r app\build\outputs\apk\debug\app-debug.apk` → `adb shell am start -n com.verse.movieverse/.MainActivity`
**Change history:** §14

> **Status (2026-10-04):** ALIVE dan **ter-push**. Layer Compose 4 tab + 3 layar non-tab, Room untuk data lokal, Retrofit ke TMDB. Build hijau (`BUILD SUCCESSFUL`), APK terpasang dan ter-smoke-test di POCO X7 (API 35) — semua 4 tab bottom-nav bisa dinavigasi tanpa crash. Perubahan terbaru: `14445c4` (perbaikan kompilasi + Guidebook ini).

---

## 1. Purpose / What It Is

MovieVerse adalah aplikasi jurnal film lokal yang berfungsi penuh di atas API TMDB. Tujuannya sederhana: pengguna menemukan film, menyimpannya ke watchlist, menandai yang sudah ditonton, dan menulis ulasan singkat dengan rating — semuanya tetap di perangkat tanpa perlu akun.

**Scope — masuk:** discovery film (trending/now-playing/genre), pencarian, detail film, watchlist, log "sudah ditonton", ulasan + rating, riwayat pencarian, profil pengguna lokal.

**Scope — keluar (sengaja tidak ada):** sinkronisasi antar perangkat, login/akun TMDB, streaming atau playback video, mode offline penuh (butuh cache jaringan yang belum ada), dan rekomendasi personalisasi.

---

## 2. Hardware & Environment

| Item | Nilai |
|---|---|
| Bahasa | Kotlin 2.2.10, Jetpack Compose (BOM 2026.02.01) |
| Build system | Gradle 9.6.0 (wrapper), AGP 9.4.1, KSP 2.3.9 |
| JDK target | Java 11 (source & target) |
| SDK | `compileSdk`/`targetSdk` 37, `minSdk` 24 |
| OS | Windows, PowerShell 5.1 |
| SDK Android | `C:\Users\ThinkPad\AppData\Local\Android\Sdk` (diisi lewat `local.properties` → `sdk.dir`) |
| `adb` | `%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe` |
| Perangkat uji | POCO X7 (`24095PCADG`), serial `CAVGPZAQ8DSGAEX8`, Android 15 / API 35, arm64-v8a, layar 1220×2712 |

Tidak ada backend milik project ini. Seluruh data film datang dari API pihak ketiga (TMDB); satu-satunya storage lokal adalah SQLite (Room) dan DataStore di perangkat.

---

## 3. Software Components

| Komponen | Versi | Peran |
|---|---|---|
| `androidx.compose:compose-bom` | 2026.02.01 | Seluruh UI |
| `material3` + `material-icons-extended` | via BOM | Design system, ikon tab |
| `androidx.navigation:navigation-compose` | 2.10.2 | Navigasi type-safe (7 rute) |
| `androidx.room:room-runtime` + `room-ktx` | 2.8.5 | DB lokal (compile via KSP) |
| `androidx.datastore:datastore-preferences` | 1.1.1 | Preferensi profil (nama, foto) |
| `com.squareup.retrofit2:retrofit` | 3.0.0 | Klien HTTP TMDB |
| `retrofit-converter-gson` | via retrofit | Serialisasi JSON (Gson, bukan kotlinx-json) |
| `io.coil-kt:coil-compose` | 2.6.0 | Muat poster film |
| `androidx.lifecycle:*` | 2.11.0 | ViewModel + komposisi |
| `androidx.activity:activity-compose` | 1.13.0 | `MainActivity` |
| `androidx.core:core-ktx` | 1.19.1 | Utilitas Android |

Plugin: `com.android.application`, `org.jetbrains.kotlin.plugin.compose`, `org.jetbrains.kotlin.plugin.serialization`, `com.google.devtools.ksp`.

> **Catatan konsistensi:** UI dan nama genre berbahasa Indonesia, tetapi `MovieRepository` meminta data TMDB dengan `language = "en-US"`. Judul/overview film akan berbahasa Inggris. Ini pilihan saat ini, bukan bug — ubah `LANGUAGE` di `MovieRepository` bila ingin id-ID.

---

## 4. Features & Scope

| Layar | Rute | Isi |
|---|---|---|
| Jelajah | `Jelajah` | Hero/populer + now-playing (grid poster, buka detail) |
| Kategori | `Kategori` | Pilih genre (peta 19 genre TMDB), hasil → `HasilPencarian` |
| Jurnal | `Jurnal` | Watchlist, sudah ditonton, ulasan, plus statistik |
| Akun | `Akun` | Profil (nama/foto dari DataStore), statistik, reset data lokal |
| Pencarian Aktif | `PencarianAktif` | Input pencarian, riwayat pencarian (Room) |
| Hasil Pencarian | `HasilPencarian(query)` | Grid hasil, bisa tambah watchlist |
| Detail Film | `DetailFilm(movieId)` | Sinopsis, rating, trailer, toggle watchlist/ditonton, form ulasan |

Interaksi lintas-layar: watchlist dan status "ditonton" bisa dicetak dari grid mana pun; navigasi dari `Kategori` bisa langsung melompat ke `Jurnal`.

---

## 5. Configuration & Credentials

Semua konfigurasi lokal ada di **`D:\movieverse\local.properties`** (sudah masuk `.gitignore`, jangan pernah di-commit):

| Key | Dibaca oleh | Keterangan |
|---|---|---|
| `sdk.dir` | Gradle | Lokasi Android SDK |
| `TMDB_TOKEN` | `app/build.gradle.kts` → `BuildConfig.TMDB_TOKEN` → OkHttp interceptor `Authorization: Bearer` | **WAJIB.** Token baca-akses TMDB v3. Tanpa ini semua layar film gagal. |
| `TMDB_API_KEY` | **tidak dibaca siapa pun** | Config mati — sisa terakhir dari pendekatan lama. Aman dihapus dari `local.properties`. |

> **Nilai token sengaja TIDAK ditulis di dokumen ini.** Sumber kebenaran tetap `local.properties`. Kalau token hilang, buat ulang di *TMDB > Settings > API > Authentication > API Read Access Token* (akun tmdb.org), lalu tempel ke `local.properties`. Standar dokumentasi mengizinkan secret inline di Guidebook internal, tapi token ini tidak perlu ikut-ikutan; kalau file ini bocor, token langsung bisa dipakai orang lain.

---

## 6. Architecture

Pola **Unidirectional Data Flow** dengan satu `sealed interface UiState<T>` untuk semua layar (loading / success / error), pesan error tunggal yang dipakai global (`PESAN_GAGAL`), dan factory ViewModel yang bisa dites tanpa DI framework.

```
[Compose Screen] --collectAsState--> [ViewModel] --suspend/--> [Repository]
                                                                        |
                                        +-------------------------------+-------------------------------+
                                        |                                                               |
                              [Retrofit MovieApi]                                          [Room AppDatabase]
                              + OkHttp Interceptor                               + PersonalDao (4 tabel)
                                (Bearer TMDB_TOKEN)                              + ProfileStore (DataStore)
                                        |                                              (nama, foto)
                                  api.themoviedb.org/3
```

Layering per domain ada dua, bukan satu:

- **Jaring / film** → `MovieRepository` (default argumen `api: MovieApi = ApiClient.movieApi`, jadi ViewModel bisa menginstansinya tanpa wiring).
- **Personal / lokal** → `PersonalRepository` (deterministik, Room + DataStore).

Alur data satu arah: `ViewModel` memanggil repository di dalam `viewModelScope`, memetakan hasil ke `UiState`, lalu Screen meng-*collect* state itu. Tidak ada `mutableStateOf` yang ditulis langsung dari luar ViewModel.

Struktur direktori:

```
app/src/main/java/com/verse/movieverse/
├── MainActivity.kt
├── data/
│   ├── local/      AppDatabase, Entities, PersonalDao, ProfileStore
│   ├── model/      Movie, GenreMap
│   ├── remote/     MovieApi (+ApiClient), MovieDto
│   └── repository/ MovieRepository, PersonalRepository
├── navigation/     Routes, AppNavigation
└── ui/
    ├── common/     UiState, Format, Share, Statistik
    ├── components/ PosterCard, PosterImage, ReviewSheet, StateSection, StatistikSection
    ├── screens/    7 layar + ViewModel-nya
    └── theme/      Color, Theme, Type
```

---

## 7. Constraints & Limitations

| Batasan | Detail |
|---|---|
| Percakapan remote | `git push` sempat gagal `403 denied to Merpatidove` (akun tanpa izin tulis). Setelah kredensial diganti, push berhasil — `main` di `origin` sekarang=`14445c4`. Kalau 403 muncul lagi, cek akun yang terautentikasi, bukan kode. |
| Tidak ada tes otomatis | Source set `test`/`androidTest` dan dependency tes sudah dihapus; verifikasi dilakukan manual lewat `assembleDebug` + logcat. |
| Butuh token valid | Token tertanam di APK saat build (bukan rahasia yang bisa dirahasiakan dari APK sendiri). Untuk distribusi publik, ini hanya aman kalau token read-only. |
| Data lokal tidak terenkripsi | Room dalam plaintext; `backup_rules.xml` masih menyertakan data. Isi jurnal pengguna bisa bocor lewat backup. |
| `minSdk 24` | APK tetap bisa jalan di Android 7.0; sebagian fitur Compose modern mungkin butuh guard. |
| Nama file tidak konsisten | `DetailFIlmViewModel.kt` memakai kapital `I` di tengah (`FIlm`). Pertahankan; jangan "diperbaiki" tanpa search-and-replace seluruh repo. |

---

## 8. Deployment & Redeploy

```powershell
# 1. Build
.\gradlew.bat :app:assembleDebug --console=plain

# 2. Pasang (tambah -r untuk update, tanpa uninstall)
$adb = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'
& $adb install -r app\build\outputs\apk\debug\app-debug.apk

# 3. Jalankan
& $adb shell am start -n com.verse.movieverse/.MainActivity
```

Kalau `adb install` gagal dengan `INSTALL_FAILED_UPDATE_INCOMPATIBLE` atau `DELETE_FAILED_INTERNAL_ERROR`, uninstall dulu (`adb uninstall com.verse.movieverse`) lalu pasang ulang — ini terjadi karena signature debug berbeda dari build sebelumnya.

Output APK: `app\build\outputs\apk\debug\app-debug.apk` (± 19,8 MiB).

---

## 9. Testing & Verification

Tidak ada framework tes. Verifikasi yang dilakukan pada 2026-10-04:

| Cek | Cara | Hasil |
|---|---|---|
| Kompilasi | `.\gradlew.bat :app:assembleDebug` | `BUILD SUCCESSFUL` |
| Instalasi | `adb install` setelah uninstall | Berhasil |
| Process hidup | `adb shell pidof com.verse.movieverse` | PID `24565` |
| Activity teratas | `adb shell dumpsys activity activities` | `topResumedActivity=com.verse.movieverse/.MainActivity` |
| Navigasi | `adb shell input tap` di keempat tab | 4/4 tab sukses dipindah |
| Crash | `adb logcat` grep `FATAL EXCEPTION`, `SerializationException`, `NoSuchMethodError`, `NoClassDefFoundError` | Tidak ada |

Navigasi diuji dengan koordinat (layar 1220×2712, y = 2602): Jelajah `x=152`, Kategori `x=457`, Jurnal `x=762`, Akun `x=1067`.

> Pemeriksaan visual lewat screenshot tidak dilakukan otomatis — verifikasi di atas berbasis logcat/PID/lifecycle. Kalau butuh cek tampilan, ambil `adb exec-out screencap -p > shot.png` dan lihat sendiri.

---

## 10. Known Issues

1. **`README.md` kosong.** Ini satu-satunya dokumen publik, dan isinya nol byte. Standar §2 mewajibkan README sebagai pintu depan publik. Butuh ditulis ulang (ringkas: apa MovieVerse, cara install, cara pakai).
2. **Push 403 sempat terjadi, sekarang resolved.** `main` sudah sinkron dengan `origin` di `14445c4`. Lihat §7 kalau minta push ditolak.
3. **`AGENTS.MD` dan `AGENTS.md` keduanya terhapus di working tree, belum di-commit.** Repo ini melacak dua file dengan nama yang berbeda kapitalisasi, yang bentrok di filesystem Windows.
4. **9 file `.idea/` masih ter-track** meskipun `.gitignore` sudah memuat `.idea/`. Perlu `git rm -r --cached .idea` untuk benar-benar berhenti dilacak.
5. **`TMDB_API_KEY` mati** di `local.properties` — sisa tak terpakai.
6. **DataStore belum dimigrasi** ke SharedPreferences; `ProfileStore` masih memakai Preferences DataStore. Ini temuan audit yang sengaja tidak dikerjakan.
7. **`TopLevelTab` masih ada** di `AppNavigation` — struktur data yang bisa dirampingkan, tapi belum membereskan refactor.

---

## 11. How to Read This Guidebook

Baca §1 untuk tahu apa ini dan apa yang **tidak** termasuk. Baca §5 sebelum build — di situ token TMDB berasal. Baca §6 kalau mau menambah layar atau sumber data baru (IKUTI pola UiState + Repository, jangan langsung panggil API dari Screen). §10 adalah daftar utang yang harus diberantas; §14 memberi urutan perubahan terbaru.

Kalau kamu cuma mau menjalankan ulang persis kondisi sekarang: ikuti §8 dari atas sampai bawah, setelah §5 dipastikan tokennya isi.

---

## 12. Project-Specific Reference

**Endpoint TMDB v3 yang dipakai** (`MovieApi`):

| Fungsi | Endpoint |
|---|---|
| `discover` | `GET discover/movie` — filter `with_genres`, `sort_by`, `vote_count.gte`, `primary_release_date.lte` |
| `nowPlaying` | `GET movie/now_playing` |
| `searchMovie` | `GET search/movie` |
| `movieDetail` | `GET movie/{id}` dengan `append_to_response` (video/trailer) |

**Skema Room** (`AppDatabase`, tabel):

| Tabel | PK | Isi |
|---|---|---|
| `reviews` | `movieId` | judul, poster, tahun, rating, catatan, tanggal tonton, flag rewatch |
| `watchlist` | `movieId` | judul, poster, tahun, waktu ditambahkan |
| `watched` | `movieId` | judul, poster, tahun, waktu ditonton |
| `search_history` | `keyword` | kata kunci, waktu cari |

**Genre:** `GenreMap` memetakan 19 `genre_id` TMDB ke nama Indonesia ("Aksi" = 28, "Petualangan" = 12, dst.). `namaDariId` untuk label tampilan, `idDariNama` untuk tombol kategori. Map balik dibangun sekali di `object` init supaya tidak di-loop tiap call.

---

## 13. Deployment / Dist Issues

- Tidak ada pipeline CI/CD, tidak ada signing release, tidak ada distributable (Play Store / APK rilis). Semua build dan installasi dilakukan manual satu mesin.
- Token TMDB di-*compile* ke dalam `BuildConfig` lalu ikut ter-*bundle* di dalam APK. Untuk rilis publik ini tidak problematis selama token-nya read-only, tapi jangan pernah pakai token dengan hak tulis.
- Build lokal selalu menghasilkan debug signing key yang berbeda antar mesin, jadi `install -r` dari mesin lain akan gagal dan butuh `uninstall` lebih dulu.

---

## 14. Change History

### 2026-10-04 — Perbaikan kompilasi + verifikasi perangkat (Merpatidove)

Empat file layar diperbaiki agar bisa dikompilasi: urutan cabang `when` di `DetailFilmScreen`, `HasilPencarianScreen`, dan `PencarianAktifScreen` memakai `is UiState.Loading, is UiState.Error`, dan `import androidx.compose.ui.unit.dp` dipulihkan di `JelajahScreen`. Setelah itu build hijau, APK dipasang ke POCO X7, dan keempat tab bottom-nav dinavigasi tanpa crash — penting karena `kotlinx-serialization-json` tidak lagi jadi dependency, jadi rute `@Serializable` harus dibuktikan masih jalan.

- **File/komponen:** `DetailFilmScreen.kt`, `HasilPencarianScreen.kt`, `PencarianAktifScreen.kt`, `JelajahScreen.kt`
- **Detail:** perbaikan kompilasi post-commit; verifikasi `BUILD SUCCESSFUL` + 4/4 tab OK
- **Status:** **committed & pushed** (`14445c4`) — sudah terpasang di perangkat dan sudah sampai remote

### 2026-10-04 — Refactor: satu komponen untuk state, stats, format, factory (`1ab8bde`) (Merpatidove)

Commit refactor utama. Template screen, komponen duplikat, dan dependency yang tidak terpakai dihapus; komponen bersama (`StateSection`, `StatistikSection`, `Format`, `Share`, `Statistik`) dan pola `viewModelFactory` diperkenalkan.

- **File/komponen:** `ui/common/*`, `ui/components/StateSection.kt`, `ui/components/StatistikSection.kt`, `data/repository/MovieRepository.kt`, `navigation/AppNavigation.kt`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `.gitignore`
- **Detail:** satu komponen untuk state, stats, format, factory; `kotlinx-serialization-json` dihapus (rute cukup butuh `-core` transitif); dependency tes dihapus; `local.properties` masuk `.gitignore`
- **Status:** **committed & pushed** (`1ab8bde`)

### 2026-10-03 — Sistem ulasan di local storage (`d6bf5bb`) (Zaqpurpur-Neo)

Menambah entitas `reviews` dan alur simpa/baca ulasan di repository personal.

- **File/komponen:** `data/local/Entities.kt`, `data/repository/PersonalRepository.kt`, `ui/components/ReviewSheet.kt`
- **Detail:** ulasan berisi rating, catatan, tanggal tonton, flag rewatch
- **Status:** committed & pushed

### 2026-10-03 — Simpan & check "sudah ditonton" (`2038e7d`) (Zaqpurpur-Neo)

Tabel `watched` plus toggle status tonton di UI.

- **File/komponen:** `data/local/Entities.kt`, `ui/screens/JurnalScreen.kt`
- **Detail:** Movies bisa ditandai sudah ditonton dari grid maupun detail
- **Status:** committed & pushed

### 2026-10-03 — Room DB untuk local storage & simpan pribadi (`7f15291`) (Zaqpurpur-Neo)

Fondasi penyimpanan lokal: Room, `PersonalDao`, `AppDatabase`, dan tabel `watchlist` + `search_history`.

- **File/komponen:** `data/local/*`, KSP di `app/build.gradle.kts`
- **Detail:** pertama kali project punya storage lokal, sebelumnya semua dari jaringan
- **Status:** committed & pushed
