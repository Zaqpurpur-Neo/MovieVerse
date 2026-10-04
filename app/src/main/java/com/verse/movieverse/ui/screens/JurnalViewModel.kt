package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.data.local.WatchlistEntity
import com.verse.movieverse.data.local.WatchedEntity
import com.verse.movieverse.data.repository.PersonalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel layar Jurnal.
 * Data lokal Room selalu tersedia, jadi tiga daftar diekspos langsung
 * sebagai StateFlow lewat stateIn (tanpa UiState Loading/Error).
 */
class JurnalViewModel(
    private val personalRepository: PersonalRepository
) : ViewModel() {

    // stateIn mengubah Flow menjadi StateFlow dengan nilai awal daftar kosong
    val reviews: StateFlow<List<ReviewEntity>> =
        personalRepository.semuaReviews()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val watchlist: StateFlow<List<WatchlistEntity>> =
        personalRepository.semuaWatchlist()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val watched: StateFlow<List<WatchedEntity>> =
        personalRepository.semuaWatched()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Film di watchlist ditandai sudah ditonton: simpan ke watched, hapus dari watchlist
    fun onMarkWatched(item: WatchlistEntity) {
        viewModelScope.launch {
            personalRepository.tandaiDitonton(item)
        }
    }

    fun onRemoveFromWatchlist(item: WatchlistEntity) {
        viewModelScope.launch {
            personalRepository.hapusDariWatchlist(item.movieId)
        }
    }

    companion object {
        // Factory sederhana untuk menyuntikkan Repository tanpa Hilt/Koin.
        fun factory(context: Context) = viewModelFactory {
            initializer {
                JurnalViewModel(PersonalRepository(AppDatabase.getInstance(context)))
            }
        }
    }
}