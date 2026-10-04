package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.SearchHistoryEntity
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.data.repository.PersonalRepository
import com.verse.movieverse.ui.common.PESAN_GAGAL
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
                // discover() sudah mengurutkan dari populer, jadi cukup ambil 5.
                val populer = movieRepository.discover().movies.take(5)
                _uiState.update { UiState.Success(populer) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error(PESAN_GAGAL) }
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

    companion object {
        // Factory sederhana untuk menyuntikkan Repository tanpa Hilt/Koin.
        fun factory(context: Context) = viewModelFactory {
            initializer {
                val db = AppDatabase.getInstance(context)
                PencarianAktifViewModel(MovieRepository(), PersonalRepository(db))
            }
        }
    }
}