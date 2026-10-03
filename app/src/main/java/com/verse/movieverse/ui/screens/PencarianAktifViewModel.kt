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
 * ViewModel layar Pencarian Aktif.
 * Memuat 5 film paling populer sebagai rekomendasi awal.
 */
class PencarianAktifViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<List<MovieSummary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<MovieSummary>>> = _uiState.asStateFlow()

    init {
        loadPopuler()
    }

    private fun loadPopuler() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val populer = repository.getMovies()
                    .sortedByDescending { it.popularity }
                    .take(5)
                _uiState.update { UiState.Success(populer) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun retry() {
        loadPopuler()
    }
}