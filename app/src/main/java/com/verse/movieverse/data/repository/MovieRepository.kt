package com.verse.movieverse.data.repository

import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.model.toDomain
import com.verse.movieverse.data.remote.ApiClient
import com.verse.movieverse.data.remote.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository sebagai sumber data tunggal (Single Source of Truth) untuk data film.
 *
 * Mengambil data dari MovieApi (Retrofit), memetakan DTO menjadi model domain,
 * dan menyediakan fungsi siap pakai untuk ViewModel.
 */
class MovieRepository(
    private val api: MovieApi = ApiClient.movieApi
) {

    companion object {
        // Cache daftar film di memori, dipakai bersama oleh SEMUA instance repository.
        // Efeknya: satu unduhan untuk semua layar (Jelajah, Kategori, Pencarian, dll).
        // @Volatile agar nilai yang ditulis satu thread langsung terlihat thread lain.
        // Cache TIDAK disimpan ke disk, jadi hilang saat aplikasi ditutup/dihentikan.
        @Volatile
        private var cache: List<MovieSummary>? = null
    }

    /**
     * Mengambil seluruh daftar ringkasan film.
     * Bila cache sudah terisi, kembalikan tanpa menyentuh jaringan.
     */
    suspend fun getMovies(): List<MovieSummary> {
        // Baca sekali ke variabel lokal agar aman dari perubahan antar-baca (pola @Volatile).
        val cached = cache
        if (cached != null) return cached

        // Panggil API TERLEBIH DAHULU. Bila gagal, exception naik ke ViewModel
        // dan cache tetap null -> tombol "Coba Lagi" akan memanggil jaringan lagi.
        val dtos = api.getMovies()

        // Pemetaan ratusan DTO dipindah ke Dispatchers.Default agar tidak membebani thread UI.
        val domain = withContext(Dispatchers.Default) {
            dtos.map { it.toDomain() }
        }

        // Hanya simpan ke cache setelah pemetaan sukses.
        cache = domain
        return domain
    }

    /**
     * Mengambil detail lengkap satu film berdasarkan ID.
     * Tidak di-cache: tiap film berbeda dan pemetaannya hanya satu objek (trivial),
     * jadi tidak perlu pindah thread.
     */
    suspend fun getMovieDetail(id: Int): MovieDetail {
        return api.getMovieDetail(id).toDomain()
    }
}