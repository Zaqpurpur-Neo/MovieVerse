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

    // === Ulasan (Fase 6B) ===
    // Amati satu ulasan milik film; null bila belum ada ulasan.
    // Memakai Flow daftar ulasan lalu dicari per movieId (tanpa mengubah DAO).
    fun observeReview(movieId: Int): Flow<ReviewEntity?> =
        dao.getAllReviews().map { list -> list.find { it.movieId == movieId } }

    // Simpan ulasan SEKALIGUS menandai film Sudah Ditonton.
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

    // === Watchlist (Fase 6A) ===
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

    // === Watched (Fase 6A) ===
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
}