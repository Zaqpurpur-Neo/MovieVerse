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
 * ViewModel menyimpan state agar tidak hilang saat konfigurasi berubah (misal: layar diputar).
 * UI hanya membaca uiState (Unidirectional Data Flow / UDF).
 */
class JelajahViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<List<MovieSummary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<MovieSummary>>> = _uiState.asStateFlow()

    // State filter disimpan di ViewModel dan diubah lewat fungsi (UDF).
    private val _selectedGenre = MutableStateFlow("Semua")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val movies = repository.getMovies()
                _uiState.update { UiState.Success(movies) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun retry() {
        loadMovies()
    }

    fun selectGenre(genre: String) {
        _selectedGenre.value = genre
    }
}