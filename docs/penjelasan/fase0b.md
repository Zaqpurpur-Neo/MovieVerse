# Penjelasan Fase 0B: Navigasi Compose Type-Safe dan Layar Placeholder

## 1. Berkas yang Dibuat atau Diubah
- **`app/src/main/java/com/verse/movieverse/navigation/Routes.kt`** (Dibuat)
  Mendefinisikan rute-rute navigasi bertipe aman (*type-safe*) menggunakan anotasi `@Serializable` dari Kotlin Serialization untuk 4 tab utama dan 3 layar pendukung.
- **`app/src/main/java/com/verse/movieverse/navigation/AppNavigation.kt`** (Dibuat)
  Menyediakan komponen `AppNavigation` yang membungkus `Scaffold`, `NavigationBar` (bottom bar) kondisional untuk 4 tab utama, dan `NavHost` yang memetakan setiap rute ke composable layar yang sesuai.
- **`app/src/main/java/com/verse/movieverse/ui/screens/PlaceholderScreens.kt`** (Dibuat)
  Menyediakan 7 fungsi composable placeholder sementara untuk memverifikasi alur navigasi dan transfer argumen dengan pola *state hoisting*.
- **`app/src/main/java/com/verse/movieverse/MainActivity.kt`** (Diubah)
  Mengatur tampilan utama aktivitas agar memuat `AppNavigation()` di dalam tema `MovieVerseTheme`.

## 2. Alur Navigasi dan Data (Contoh: Menuju Layar Detail Film)
1. **Ketukan Pengguna**: Pengguna mengetuk tombol `"Buka Detail (id 157336)"` di `JelajahScreen`.
2. **State Hoisting**: Event ketukan memanggil callback lambda `onOpenDetail(157336)` yang diteruskan ke `AppNavigation`.
3. **Navigasi Type-Safe**: `AppNavigation` menjalankan `navController.navigate(DetailFilm(movieId = 157336))`. Objek rute ini secara otomatis diserialisasi dan disimpan ke dalam bundle navigasi.
4. **Ekstraksi Argumen**: Destinasi `composable<DetailFilm>` menerima `NavBackStackEntry`, lalu mengekstrak argumen dengan `backStackEntry.toRoute<DetailFilm>()`.
5. **Tampilan Layar**: Nilai `args.movieId` (157336) dikirim ke parameter `DetailFilmScreen(movieId = ...)` dan ditampilkan di layar.

## 3. Materi Kuliah yang Diterapkan
- **Navigation Compose Type-Safe (Materi 7)**:
  - Diterapkan pada `Routes.kt` (`@Serializable object Jelajah`, `@Serializable data class DetailFilm(val movieId: Int)`).
  - Diterapkan pada `AppNavigation.kt` (`NavHost`, `composable<...>`, dan `backStackEntry.toRoute<...>()`).
- **Scaffold & Bottom Navigation Bar (Materi 2 & 7)**:
  - Diterapkan pada `AppNavigation.kt` menggunakan `Scaffold`, `NavigationBar`, dan `NavigationBarItem` dengan visibilitas otomatis (hanya muncul di 4 tab utama).
- **State Hoisting & UDF (Materi 3)**:
  - Diterapkan pada `PlaceholderScreens.kt` di mana layar tidak memegang `NavController`, melainkan menerima event berupa fungsi lambda (`onOpenDetail`, `onOpenSearch`, `onNavigateUp`).

## 4. Hal yang Sengaja Disederhanakan atau Dilewati
- Seluruh 7 layar masih berupa UI placeholder sederhana dalam satu berkas (`PlaceholderScreens.kt`) untuk memvalidasi rute navigasi; pemisahan satu berkas per layar beserta ViewModel dan UiState akan dikerjakan pada fase-fase berikutnya.
- Belum ada integrasi ViewModel, Repository, Room, maupun pemanggilan jaringan Retrofit sesuai cakupan Fase 0B.
