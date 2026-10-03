package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.data.repository.PersonalRepository
import com.verse.movieverse.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel layar Detail Film.
 * Alur: Screen memanggil muat(id) -> ViewModel -> Repository -> sumber data.
 */
class DetailFilmViewModel(
    private val movieRepository: MovieRepository,
    private val personalRepository: PersonalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MovieDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<MovieDetail>> = _uiState.asStateFlow()

    // State tombol Simpan (watchlist) dan Sudah Ditonton
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    private val _isWatched = MutableStateFlow(false)
    val isWatched: StateFlow<Boolean> = _isWatched.asStateFlow()

    // Guard agar tidak memuat ulang saat argumen sama (misal layar diputar)
    private var idTerakhir: Int? = null

    fun muat(id: Int) {
        if (id == idTerakhir && _uiState.value is UiState.Success) return

        idTerakhir = id
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val detail = movieRepository.getMovieDetail(id)
                _uiState.update { UiState.Success(detail) }
                // Mulai amati status watchlist dan watched untuk film ini
                collectStatus(id)
            } catch (e: Exception) {
                _uiState.update { UiState.Error("Gagal memuat detail film. Periksa koneksi internet Anda.") }
            }
        }
    }

    // Kumpulkan Flow dari repository agar tombol selalu sinkron dengan Room
    private fun collectStatus(movieId: Int) {
        viewModelScope.launch {
            personalRepository.observeIsInWatchlist(movieId).collect { saved ->
                _isSaved.value = saved
            }
        }
        viewModelScope.launch {
            personalRepository.observeIsWatched(movieId).collect { watched ->
                _isWatched.value = watched
            }
        }
    }

    fun retry() {
        val id = idTerakhir ?: return
        muat(id)
    }

    // Toggle watchlist: bila ada hapus, bila belum ada simpan
    fun onToggleSave() {
        val state = _uiState.value
        if (state is UiState.Success) {
            val film = state.data
            viewModelScope.launch {
                personalRepository.toggleWatchlist(
                    movieId = film.id,
                    title = film.title,
                    posterUrl = film.posterUrl,
                    year = film.year
                )
            }
        }
    }

    // Toggle sudah ditonton: bila ada hapus, bila belum ada simpan
    fun onToggleWatched() {
        val state = _uiState.value
        if (state is UiState.Success) {
            val film = state.data
            viewModelScope.launch {
                personalRepository.toggleWatched(
                    movieId = film.id,
                    title = film.title,
                    posterUrl = film.posterUrl,
                    year = film.year
                )
            }
        }
    }

    // Factory sederhana untuk menyuntikkan Context / Repository tanpa Hilt/Koin
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = AppDatabase.getInstance(context)
            val movieRepo = MovieRepository()
            val personalRepo = PersonalRepository(db)
            @Suppress("UNCHECKED_CAST")
            return DetailFilmViewModel(movieRepo, personalRepo) as T
        }
    }
}