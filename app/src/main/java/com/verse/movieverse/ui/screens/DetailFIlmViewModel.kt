package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.data.repository.PersonalRepository
import com.verse.movieverse.ui.common.PESAN_GAGAL
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

    // Ulasan pribadi film ini (null bila belum ada)
    private val _myReview = MutableStateFlow<ReviewEntity?>(null)
    val myReview: StateFlow<ReviewEntity?> = _myReview.asStateFlow()

    // Apakah bottom sheet ulasan sedang tampil
    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet.asStateFlow()

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
                // Mulai amati status watchlist, watched, dan ulasan
                collectStatus(id)
            } catch (e: Exception) {
                _uiState.update { UiState.Error(PESAN_GAGAL) }
            }
        }
    }

    // Kumpulkan Flow dari repository agar UI selalu sinkron dengan Room
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
        viewModelScope.launch {
            personalRepository.observeReview(movieId).collect { review ->
                _myReview.value = review
            }
        }
    }

    fun retry() {
        val id = idTerakhir ?: return
        muat(id)
    }

    // Toggle watchlist: bila ada hapus, bila belum ada simpan
    fun onToggleSave() {
        withFilm { film ->
            personalRepository.toggleWatchlist(
                movieId = film.id,
                title = film.title,
                posterUrl = film.posterUrl,
                year = film.year
            )
        }
    }

    // Toggle sudah ditonton: bila ada hapus, bila belum ada simpan
    fun onToggleWatched() {
        withFilm { film ->
            personalRepository.toggleWatched(
                movieId = film.id,
                title = film.title,
                posterUrl = film.posterUrl,
                year = film.year
            )
        }
    }

    // === Bottom sheet ulasan (Fase 6B) ===

    fun onOpenSheet() {
        _showSheet.value = true
    }

    fun onCloseSheet() {
        _showSheet.value = false
    }

    fun onSaveReview(
        rating: Float,
        tanggal: String,
        isRewatch: Boolean,
        catatan: String
    ) {
        withFilm { film ->
            personalRepository.simpanReview(
                movieId = film.id,
                title = film.title,
                posterUrl = film.posterUrl,
                year = film.year,
                rating = rating,
                note = catatan,
                watchedDate = tanggal,
                isRewatch = isRewatch
            )
            _showSheet.value = false
        }
    }

    fun onDeleteReview() {
        withFilm { film ->
            personalRepository.hapusReview(film.id)
            _showSheet.value = false
        }
    }

    /**
     * Actionsame-filmdilewatseoranghelper:ambilfilmdariUiState,
     * lalujalankannya di dalamviewModelScope.
     */
    private fun withFilm(aksi: suspend (MovieDetail) -> Unit) {
        val state = _uiState.value
        if (state !is UiState.Success) return
        viewModelScope.launch { aksi(state.data) }
    }

    companion object {
        // Factory sederhana untuk menyuntikkan Repository tanpa Hilt/Koin.
        fun factory(context: Context) = viewModelFactory {
            initializer {
                val db = AppDatabase.getInstance(context)
                DetailFilmViewModel(MovieRepository(), PersonalRepository(db))
            }
        }
    }
}