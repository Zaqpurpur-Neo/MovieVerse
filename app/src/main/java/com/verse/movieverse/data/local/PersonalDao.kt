package com.verse.movieverse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalDao {
    // === Riwayat Pencarian ===
    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT 10")
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(entity: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE keyword = :keyword")
    suspend fun deleteSearchHistory(keyword: String)

    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()

    // === Reviews (Fase 6B) ===
    @Query("SELECT * FROM reviews")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(entity: ReviewEntity)

    @Query("DELETE FROM reviews WHERE movieId = :movieId")
    suspend fun deleteReview(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM reviews WHERE movieId = :movieId)")
    suspend fun hasReview(movieId: Int): Boolean

    // === Watchlist (Fase 6A) ===
    @Query("SELECT * FROM watchlist")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun deleteWatchlist(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
    suspend fun isInWatchlist(movieId: Int): Boolean

    // Observasi status watchlist secara reaktif (Flow)
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
    fun isInWatchlistFlow(movieId: Int): Flow<Boolean>

    // === Watched (Fase 6A) ===
    @Query("SELECT * FROM watched")
    fun getAllWatched(): Flow<List<WatchedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatched(entity: WatchedEntity)

    @Query("DELETE FROM watched WHERE movieId = :movieId")
    suspend fun deleteWatched(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM watched WHERE movieId = :movieId)")
    suspend fun isWatched(movieId: Int): Boolean

    // Observasi status watched secara reaktif (Flow)
    @Query("SELECT EXISTS(SELECT 1 FROM watched WHERE movieId = :movieId)")
    fun isWatchedFlow(movieId: Int): Flow<Boolean>
}