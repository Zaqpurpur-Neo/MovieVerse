# AGENTS.md - MovieVerse

## Proyek
Aplikasi Android review film bergaya Letterboxd, tugas UTS Pemrograman
Mobile. Tidak dipublikasikan. Package: com.verse.movieverse, minSdk 24.
Bahasa UI: Indonesia. Nama kode (kelas, fungsi, variabel): Inggris.
Komentar kode: Indonesia, singkat.

## Baca dulu setiap sesi
1. docs/design/adjustments.md  (NORMATIF, mengalahkan berkas lain)
2. docs/design/screens-spec.md (komponen, data, aksi, state per layar)
3. docs/design/design-tokens.md (warna M3, tipografi, bentuk)
4. docs/design/<layar>/screenshot.png dan index.html (referensi visual)
Jika berkas-berkas itu bertentangan, urutan prioritas: adjustments.md,
lalu screens-spec.md, lalu screenshot/HTML.

## Prinsip utama: KODE HARUS SEDERHANA
Pemilik proyek harus bisa menjelaskan SETIAP berkas saat presentasi.
- Satu layar = satu berkas Screen + satu ViewModel + satu UiState.
- Tanpa abstraksi tambahan: tanpa use case, mapper berlapis, base
  class, interface yang hanya punya satu implementasi.
- Fungsi pendek (idealnya di bawah 30 baris), nama jelas.
- Hindari fitur rumit: generics lanjutan, extension berantai, custom
  layout, animasi kompleks, reflection, coroutine operator tingkat
  lanjut. Pilih cara yang paling mudah dibaca, bukan yang paling ringkas.
- Komentar Indonesia hanya untuk konsep yang dinilai kuliah: state
  hoisting, key pada Lazy, UiState, rute @Serializable, Repository.
- Jika sebuah fitur menuntut kode rumit, JANGAN dipaksakan:
  sederhanakan atau lewati, lalu laporkan di akhir sesi.

## Aturan kerja agen (WAJIB)
- SATU fase per sesi. Kerjakan hanya fase yang diminta prompt.
- Dilarang: git commit, git push, git revert, git reset.
- Stitch hanya DIBACA (jangan generate atau edit layar).
- Versi library HANYA di gradle/libs.versions.toml. Jangan menulis
  angka versi di build.gradle.kts, dan jangan mengarang versi.
- Jangan menambah dependency di luar yang disebut prompt fase.
  Room dan KSP ditambahkan SENDIRIAN di Fase 5, di sesi tersendiri.
- Jangan mengubah build.gradle.kts, libs.versions.toml, atau
  AndroidManifest.xml kecuali prompt fase menyuruhnya.
- Jangan membuat berkas di luar yang dibutuhkan fase.
- Akhir sesi: pastikan ./gradlew :app:assembleDebug berhasil. Jika
  gagal dan tidak bisa diperbaiki dalam lingkup fase, laporkan pesan
  error apa adanya. Jangan menutupi atau menghapus fitur diam-diam.
- Akhir sesi: tulis docs/penjelasan/faseN.md (lihat bagian bawah).

## Teknologi (keputusan final)
- Kotlin, Jetpack Compose, Material 3 (dark, ungu pastel, Roboto Flex).
- Navigasi: Navigation Compose type-safe (rute @Serializable).
  BUKAN Navigation 3. Jangan memakai paket androidx.navigation3.
- Arsitektur: MVVM. ViewModel + StateFlow + Repository.
  UiState sealed interface: Loading, Success, Error.
  Tanpa framework DI (Hilt/Koin dilarang). ViewModel dibuat dengan
  viewModel() dan Factory sederhana bila perlu.
- Jaringan: Retrofit + converter Gson. DTO WAJIB nullable
  (semua field bertipe nullable dengan nilai default null).
- Gambar: Coil 2.x. Import: coil.compose.AsyncImage.
- kotlinx.serialization HANYA untuk rute navigasi, bukan untuk JSON data.
- Penyimpanan pribadi: Room (Fase 5): tabel review (ulasan pribadi),
  watchlist, watched, dan search_history (id, keyword).
- DataStore: nama dan path foto profil.
- Tanpa TMDB API, tanpa akun/login, tanpa server autentikasi, tanpa
  review komunitas.

## Data film
- Sumber: The Movies Dataset (Kaggle) yang diolah menjadi JSON statis,
  di-host di GitHub Pages (repo terpisah movieverse-data). Film sampai
  Juli 2017. Trailer dari MovieLens 20M YouTube Trailers (CC BY 4.0),
  tidak semua film punya trailer.
- Poster dimuat dari image.tmdb.org. Jika gagal: tampilkan poster
  placeholder. Tidak ada backdrop; hero memakai poster diperbesar
  dengan gradien gelap.
- Tidak ada: sertifikasi usia, foto aktor, vote count, Film Serupa.
- File mentah ada di data-source/ (gitignore). JANGAN di-commit.

## Trailer
- WebView + iframe YouTube embed.
- Jebakan Error 153: kirim referrer. Pakai referrerpolicy="strict-origin"
  pada iframe dan loadDataWithBaseURL dengan base URL https.
- WAJIB ada tombol cadangan "Buka di YouTube" (Intent ACTION_VIEW).
- Thumbnail trailer diturunkan dari ID YouTube, tidak disimpan di JSON.

## Struktur navigasi
- Tab bawah: Jelajah, Kategori, Jurnal, Akun.
- Layar lain: Detail Film, Hasil Pencarian, Pencarian Aktif.
- Bottom sheet (ModalBottomSheet): Tulis Ulasan dan Edit Ulasan.
- Watchlist dan Sudah Ditonton adalah tab di dalam layar Jurnal.
- Kirim ke Detail Film hanya movieId (bukan objek film utuh).

## Rubrik kuliah (ketujuh materi harus terlihat jelas di kode)
1 Layout dasar | 2 Material 3 (tema, tombol, OutlinedTextField, Card) |
3 State (remember, rememberSaveable, state hoisting, UDF) |
4 Lazy layouts (parameter key WAJIB di items) | 5 Retrofit |
6 MVVM + ViewModel + UiState | 7 Navigation Compose type-safe,
minimal 3 layar, transfer data antar layar, Scaffold + bottom bar.

## Fase
0 Fondasi (tema, navigasi, placeholder; buang sisa kode Nav3 lama)
1 Siapkan data (dataset, JSON, hosting)
2 Jaringan (Retrofit, repository, UiState)
3 Jelajah dan Pencarian
4 Detail + trailer
5 Room (sendirian) + riwayat pencarian
6 Simpan, Ditonton, Ulasan pribadi (Jurnal + bottom sheet)
7 Akun dan statistik
8 Pemolesan

## Penjelasan per fase (WAJIB, akhir setiap sesi)
Tulis docs/penjelasan/faseN.md berisi:
1. Daftar berkas yang dibuat/diubah dan fungsi tiap berkas (1-2 kalimat).
2. Alur data SATU layar dari ketukan pengguna sampai tampil
   (Screen -> ViewModel -> Repository -> sumber data).
3. Materi kuliah mana yang diterapkan, di berkas dan fungsi apa.
4. Hal yang sengaja disederhanakan atau dilewati.
Bahasa: Indonesia sederhana, tanpa istilah yang tidak perlu.

## Di luar cakupan (JANGAN dibuat)
Film Serupa, pilihan Bahasa, sentimen tayangan, badge sertifikasi usia,
tren persentase, badge Prioritas, login/akun, komentar komunitas,
notifikasi, sinkronisasi cloud, DI framework, modularisasi.
