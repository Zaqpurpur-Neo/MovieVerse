package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.SearchHistoryEntity
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.data.repository.PersonalRepository
import com.verse.movieverse.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PencarianAktifViewModel(
    private val movieRepository: MovieRepository,
    private val personalRepository: PersonalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MovieSummary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<MovieSummary>>> = _uiState.asStateFlow()

    val searchHistory: Flow<List<SearchHistoryEntity>> = personalRepository.getSearchHistory()

    init {
        loadPopuler()
    }

    private fun loadPopuler() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val populer = movieRepository.getMovies()
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

    fun saveSearch(keyword: String) {
        viewModelScope.launch {
            personalRepository.saveSearchHistory(keyword)
        }
    }

    fun deleteSearch(keyword: String) {
        viewModelScope.launch {
            personalRepository.deleteSearchHistory(keyword)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            personalRepository.clearSearchHistory()
        }
    }

    // Factory sederhana untuk menyuntikkan Context / Repository tanpa Hilt/Koin
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = AppDatabase.getInstance(context)
            val movieRepo = MovieRepository()
            val personalRepo = PersonalRepository(db)
            @Suppress("UNCHECKED_CAST")
            return PencarianAktifViewModel(movieRepo, personalRepo) as T
        }
    }
}