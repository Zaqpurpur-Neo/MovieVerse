package com.verse.movieverse.data.repository

import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

class PersonalRepository(private val database: AppDatabase) {
    private val dao = database.personalDao()

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
}