package com.verse.movieverse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GenreItem(val nama: String, val jumlah: Int)
data class KoleksiItem(val judul: String, val deskripsi: String, val jumlah: Int, val posterUrl: String)
data class KategoriData(val genres: List<GenreItem>, val koleksi: List<KoleksiItem>)

/**
 * Data diolah di ViewModel, layar hanya menampilkan (UDF).
 */
class KategoriViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<KategoriData>>(UiState.Loading)
    val uiState: StateFlow<UiState<KategoriData>> = _uiState.asStateFlow()

    init {
        loadKategori()
    }

    private fun loadKategori() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val movies = repository.getMovies()
                val data = buatKategoriData(movies)
                _uiState.update { UiState.Success(data) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun retry() {
        loadKategori()
    }

    private fun buatKategoriData(movies: List<MovieSummary>): KategoriData {
        val daftarGenre = listOf("Aksi", "Petualangan", "Sci-Fi", "Drama", "Horor", "Animasi", "Thriller", "Komedi")

        val genres = daftarGenre.map { namaGenre ->
            val jumlah = movies.count { movie -> movie.genres.contains(namaGenre) }
            GenreItem(namaGenre, jumlah)
        }

        val skorTertinggi = movies.filter { it.rating >= 8.0 }
        val klasik = movies.filter { it.year < 1990 }
        val baru = movies.filter { it.year >= 2016 }

        val koleksi = listOf(
            KoleksiItem(
                judul = "Skor Tertinggi",
                deskripsi = "Film dengan rating 8.0 ke atas",
                jumlah = skorTertinggi.size,
                posterUrl = skorTertinggi.maxByOrNull { it.popularity }?.posterUrl.orEmpty()
            ),
            KoleksiItem(
                judul = "Klasik Sepanjang Masa",
                deskripsi = "Film klasik rilis sebelum 1990",
                jumlah = klasik.size,
                posterUrl = klasik.maxByOrNull { it.popularity }?.posterUrl.orEmpty()
            ),
            KoleksiItem(
                judul = "Baru di Katalog",
                deskripsi = "Film rilis 2016 hingga 2017",
                jumlah = baru.size,
                posterUrl = baru.maxByOrNull { it.popularity }?.posterUrl.orEmpty()
            )
        )

        return KategoriData(genres, koleksi)
    }
}