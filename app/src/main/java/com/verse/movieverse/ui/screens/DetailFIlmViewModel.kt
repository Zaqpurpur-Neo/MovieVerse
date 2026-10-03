package com.verse.movieverse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel layar Detail Film.
 * Alur: Screen memanggil muat(id) -> ViewModel -> Repository -> API.
 */
class DetailFilmViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<MovieDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<MovieDetail>> = _uiState.asStateFlow()

    // Guard agar tidak memuat ulang saat argumen sama (misal saat layar diputar).
    private var idTerakhir: Int? = null

    fun muat(id: Int) {
        if (id == idTerakhir && _uiState.value is UiState.Success) return

        idTerakhir = id
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val detail = repository.getMovieDetail(id)
                _uiState.update { UiState.Success(detail) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat detail film. Periksa koneksi internet Anda.") }
            }
        }
    }

    fun retry() {
        val id = idTerakhir ?: return
        muat(id)
    }
}