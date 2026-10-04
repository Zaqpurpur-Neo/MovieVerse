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
 * ViewModel layar Jelajah.
 * Tiap bagian (hero, populer, sedang tayang) punya state sendiri agar
 * layar tampil bertahap, bukan menunggu semua data siap (UDF).
 */
class JelajahViewModel : ViewModel() {

    private val repository = MovieRepository()

    // Filter genre disimpan di ViewModel dan diubah lewat fungsi (UDF).
    private val _selectedGenre = MutableStateFlow("Semua")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _hero = MutableStateFlow<UiState<MovieSummary>>(UiState.Loading)
    val hero: StateFlow<UiState<MovieSummary>> = _hero.asStateFlow()

    private val _populer = MutableStateFlow<UiState<List<MovieSummary>>>(UiState.Loading)
    val populer: StateFlow<UiState<List<MovieSummary>>> = _populer.asStateFlow()

    private val _sedangTayang = MutableStateFlow<UiState<List<MovieSummary>>>(UiState.Loading)
    val sedangTayang: StateFlow<UiState<List<MovieSummary>>> = _sedangTayang.asStateFlow()

    init {
        // Tiga muat diluncurkan terpisah sehingga berjalan bersamaan.
        muatHero()
        muatPopuler()
        muatSedangTayang()
    }

    fun selectGenre(genre: String) {
        _selectedGenre.value = genre
        muatPopuler()
    }

    private fun muatHero() {
        _hero.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val page = repository.getPopular(genre = null, page = 1)
                // Ambil film pertama yang punya poster sebagai unggulan.
                val film = page.movies.firstOrNull { it.posterUrl.isNotBlank() }
                if (film != null) {
                    _hero.update { UiState.Success(film) }
                } else {
                    _hero.update { UiState.Error("Tidak ada film unggulan.") }
                }
            } catch (e: Exception) {
                _hero.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    private fun muatPopuler() {
        _populer.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val page = repository.getPopular(genre = _selectedGenre.value, page = 1)
                _populer.update { UiState.Success(page.movies) }
            } catch (e: Exception) {
                _populer.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    private fun muatSedangTayang() {
        _sedangTayang.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val page = repository.getNowPlaying(page = 1)
                _sedangTayang.update { UiState.Success(page.movies) }
            } catch (e: Exception) {
                _sedangTayang.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun retryHero() = muatHero()
    fun retryPopuler() = muatPopuler()
    fun retrySedangTayang() = muatSedangTayang()
}