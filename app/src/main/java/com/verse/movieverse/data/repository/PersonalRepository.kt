package com.verse.movieverse.data.repository

import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.SearchHistoryEntity
import com.verse.movieverse.data.local.WatchlistEntity
import com.verse.movieverse.data.local.WatchedEntity
import kotlinx.coroutines.flow.Flow

class PersonalRepository(private val database: AppDatabase) {
    private val dao = database.personalDao()

    // === Riwayat Pencarian ===
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> = dao.getSearchHistory()

    suspend fun saveSearchHistory(keyword: String) {
        val entity = SearchHistoryEntity(
            keyword = keyword,
            searchedAt = System.currentTimeMillis()
        )
        dao.insertSearchHistory(entity)
    }

    suspend fun deleteSearchHistory(keyword: String) {
        dao.deleteSearchHistory(keyword)
    }

    suspend fun clearSearchHistory() {
        dao.clearSearchHistory()
    }

    // === Watchlist (Fase 6A) ===
    // Observasi apakah film ada di watchlist (reaktif)
    fun observeIsInWatchlist(movieId: Int): Flow<Boolean> = dao.isInWatchlistFlow(movieId)

    // Toggle watchlist: bila sudah ada hapus, bila belum ada simpan
    suspend fun toggleWatchlist(movieId: Int, title: String, posterUrl: String, year: Int) {
        if (dao.isInWatchlist(movieId)) {
            dao.deleteWatchlist(movieId)
        } else {
            val entity = WatchlistEntity(
                movieId = movieId,
                title = title,
                posterUrl = posterUrl,
                year = year,
                addedAt = System.currentTimeMillis()
            )
            dao.insertWatchlist(entity)
        }
    }

    // === Watched (Fase 6A) ===
    // Observasi apakah film sudah ditonton (reaktif)
    fun observeIsWatched(movieId: Int): Flow<Boolean> = dao.isWatchedFlow(movieId)

    // Toggle watched: bila sudah ada hapus, bila belum ada simpan
    suspend fun toggleWatched(movieId: Int, title: String, posterUrl: String, year: Int) {
        if (dao.isWatched(movieId)) {
            dao.deleteWatched(movieId)
        } else {
            val entity = WatchedEntity(
                movieId = movieId,
                title = title,
                posterUrl = posterUrl,
                year = year,
                watchedAt = System.currentTimeMillis()
            )
            dao.insertWatched(entity)
        }
    }
}