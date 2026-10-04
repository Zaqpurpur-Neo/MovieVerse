package com.verse.movieverse.data.repository

import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.data.local.SearchHistoryEntity
import com.verse.movieverse.data.local.WatchlistEntity
import com.verse.movieverse.data.local.WatchedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    // === Ulasan ===
    fun observeReview(movieId: Int): Flow<ReviewEntity?> =
        dao.getAllReviews().map { list -> list.find { it.movieId == movieId } }

    suspend fun simpanReview(
        movieId: Int,
        title: String,
        posterUrl: String,
        year: Int,
        rating: Float,
        note: String,
        watchedDate: String,
        isRewatch: Boolean
    ) {
        val review = ReviewEntity(
            movieId = movieId,
            title = title,
            posterUrl = posterUrl,
            year = year,
            rating = rating,
            note = note,
            watchedDate = watchedDate,
            isRewatch = isRewatch,
            createdAt = System.currentTimeMillis()
        )
        dao.insertReview(review)
        if (!dao.isWatched(movieId)) {
            dao.insertWatched(
                WatchedEntity(movieId, title, posterUrl, year, System.currentTimeMillis())
            )
        }
    }

    suspend fun hapusReview(movieId: Int) {
        dao.deleteReview(movieId)
    }

    // === Watchlist ===
    fun observeIsInWatchlist(movieId: Int): Flow<Boolean> = dao.isInWatchlistFlow(movieId)

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

    // === Watched ===
    fun observeIsWatched(movieId: Int): Flow<Boolean> = dao.isWatchedFlow(movieId)

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

    // === Jurnal (Fase 6C) ===
    fun semuaReviews(): Flow<List<ReviewEntity>> = dao.getAllReviews()

    fun semuaWatchlist(): Flow<List<WatchlistEntity>> = dao.getAllWatchlist()

    fun semuaWatched(): Flow<List<WatchedEntity>> = dao.getAllWatched()

    // Pindahkan film dari watchlist ke watched
    suspend fun tandaiDitonton(item: WatchlistEntity) {
        dao.insertWatched(
            WatchedEntity(
                movieId = item.movieId,
                title = item.title,
                posterUrl = item.posterUrl,
                year = item.year,
                watchedAt = System.currentTimeMillis()
            )
        )
        dao.deleteWatchlist(item.movieId)
    }

    suspend fun hapusDariWatchlist(movieId: Int) {
        dao.deleteWatchlist(movieId)
    }
}