package com.verse.movieverse.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    // Statistik pribadi: fungsi pendek, dihitung langsung dari daftar
    fun jumlahFilmDinilai(): Int = reviews.value.size

    fun jumlahUlasanDitulis(): Int = reviews.value.count { it.note.isNotBlank() }

    fun rataRataSkor(): Float {
        val daftar = reviews.value
        if (daftar.isEmpty()) return 0f
        return (daftar.sumOf { it.rating.toDouble() } / daftar.size).toFloat()
    }

    fun jumlahWatchlist(): Int = watchlist.value.size

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

    // Factory sederhana untuk menyuntikkan Context / Repository tanpa Hilt/Koin
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = AppDatabase.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return JurnalViewModel(PersonalRepository(db)) as T
        }
    }
}