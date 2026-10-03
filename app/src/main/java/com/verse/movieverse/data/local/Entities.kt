package com.verse.movieverse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val posterUrl: String,
    val year: Int,
    val rating: Float,
    val note: String,
    val watchedDate: String,
    val isRewatch: Boolean,
    val createdAt: Long
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val posterUrl: String,
    val year: Int,
    val addedAt: Long
)

@Entity(tableName = "watched")
data class WatchedEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val posterUrl: String,
    val year: Int,
    val watchedAt: Long
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val keyword: String,
    val searchedAt: Long
)