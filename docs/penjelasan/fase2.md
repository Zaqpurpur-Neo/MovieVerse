# Penjelasan Fase 2: Jaringan (Retrofit, Repository, UiState)

## 1. Berkas yang Dibuat atau Diubah
- **`app/src/main/AndroidManifest.xml`** (Diubah)
  Menambahkan izin `android.permission.INTERNET` agar aplikasi dapat mengakses API jaringan.
- **`app/src/main/java/com/verse/movieverse/data/remote/MovieDto.kt`** (Dibuat)
  Menyediakan kelas data DTO (`MovieSummaryDto`, `MovieDetailDto`, `CastDto`) yang semua field-nya bertipe nullable untuk deserialisasi respons JSON secara aman.
- **`app/src/main/java/com/verse/movieverse/data/remote/MovieApi.kt`** (Dibuat)
  Mendefinisikan interface endpoint Retrofit (`getMovies`, `getMovieDetail`) dan objek singleton `ApiClient` untuk base URL data statis GitHub Pages.
- **`app/src/main/java/com/verse/movieverse/data/model/Movie.kt`** (Dibuat)
  Menyediakan domain model (`MovieSummary`, `MovieDetail`, `CastMember`) yang bersih dari nullability berlebih serta fungsi ekstensi pemetaan (`toDomain`).
- **`app/src/main/java/com/verse/movieverse/data/repository/MovieRepository.kt`** (Dibuat)
  Menyediakan lapisan *Single Source of Truth* untuk mengambil data film dari API dan memetakannya ke domain model untuk dikonsumsi ViewModel.
- **`app/src/main/java/com/verse/movieverse/ui/common/UiState.kt`** (Dibuat)
  Mendefinisikan generic sealed interface `UiState` (`Loading`, `Success`, `Error`) untuk manajemen state antarmuka berbasis *Unidirectional Data Flow*.

## 2. Alur Data Satu Layar (Contoh: Menampilkan Detail Film)
1. **Screen**: Layar meminta data film melalui ViewModel (misal saat inisialisasi dengan `movieId`).
2. **ViewModel**: Mengubah status UI State menjadi `UiState.Loading`, lalu memanggil fungsi suspend `movieRepository.getMovieDetail(movieId)` di dalam `viewModelScope`.
3. **Repository**: `MovieRepository` memanggil `api.getMovieDetail(id)` pada `MovieApi`.
4. **Sumber Data (Retrofit / Jaringan)**: Retrofit melakukan HTTP GET ke `https://zaqpurpur-neo.github.io/movieverse-data/movies/{id}.json`, mendeserialisasi JSON menjadi `MovieDetailDto` melalui Gson converter.
5. **Konversi Domain & Emisi State**: `MovieRepository` memetakan DTO menjadi objek `MovieDetail` dengan `toDomain()`. ViewModel menerima data tersebut dan mengupdate StateFlow menjadi `UiState.Success(movieDetail)`.
6. **Tampilan Screen**: Composable mengamati StateFlow dan merender data film yang berhasil dimuat.

## 3. Materi Kuliah yang Diterapkan
- **Retrofit & Jaringan (Materi 5)**:
  - Diterapkan pada `MovieApi.kt` (`@GET`, `@Path`, dan `Retrofit.Builder` dengan `GsonConverterFactory`).
  - Diterapkan pada `MovieDto.kt` untuk mapping JSON nullable.
  - Diterapkan izin internet pada `AndroidManifest.xml`.
- **Arsitektur Repository & MVVM (Materi 6)**:
  - Diterapkan pada `MovieRepository.kt` sebagai lapisan perantara yang mengabstraksi sumber data dari UI.
  - Diterapkan pada `UiState.kt` sebagai `sealed interface` (`Loading`, `Success`, `Error`) untuk mendukung pola UDF pada ViewModel.

## 4. Hal yang Sengaja Disederhanakan atau Dilewati
- Tidak menggunakan framework *Dependency Injection* pihak ketiga (tanpa Hilt / Koin) sesuai prinsip kesederhanaan; `MovieRepository` langsung menggunakan default parameter `ApiClient.movieApi`.
- Tidak menggunakan OkHttp interceptor/logging rumit; Retrofit menggunakan konfigurasi bawaan standar.
- Implementasi ViewModel dan integrasi composable UI per layar akan dikerjakan pada Fase 3 (Jelajah & Pencarian) dan Fase 4 (Detail Film).
