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

/**
 * Data hasil pencarian: kata kunci dan daftar film yang cocok.
 */
data class HasilData(
    val query: String,
    val hasil: List<MovieSummary>
)

/**
 * ViewModel layar Hasil Pencarian.
 * Pencarian dilakukan di aplikasi (data sudah diunduh), bukan di server.
 */
class HasilPencarianViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<HasilData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HasilData>> = _uiState.asStateFlow()

    // Jumlah film yang tampil di daftar; bertambah lewat tombol Muat Lebih Banyak.
    private val _jumlahTampil = MutableStateFlow(10)
    val jumlahTampil: StateFlow<Int> = _jumlahTampil.asStateFlow()

    private var queryTerakhir: String? = null

    fun cari(query: String) {
        // Jika query sama dan data sudah Success, jangan memuat ulang
        // (misal saat layar diputar dan LaunchedEffect jalan lagi).
        if (query == queryTerakhir && _uiState.value is UiState.Success) return

        queryTerakhir = query
        _jumlahTampil.value = 10
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val semua = repository.getMovies()
                val hasil = semua
                    .filter { cocok(it, query) }
                    .sortedByDescending { it.popularity }
                _uiState.update { UiState.Success(HasilData(query, hasil)) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun muatLagi() {
        _jumlahTampil.value = _jumlahTampil.value + 10
    }

    fun retry() {
        val query = queryTerakhir ?: return
        cari(query)
    }

    // Cocok bila query terkandung di judul, sutradara, pemeran, atau genre.
    private fun cocok(film: MovieSummary, query: String): Boolean {
        val q = query.lowercase()
        if (film.title.lowercase().contains(q)) return true
        if (film.director.lowercase().contains(q)) return true
        if (film.cast.any { it.lowercase().contains(q) }) return true
        if (film.genres.any { it.lowercase().contains(q) }) return true
        return false
    }
}