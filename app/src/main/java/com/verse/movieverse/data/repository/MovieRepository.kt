package com.verse.movieverse.data.repository

import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.model.toDomain
import com.verse.movieverse.data.remote.ApiClient
import com.verse.movieverse.data.remote.MovieApi

/**
 * Repository sebagai sumber data tunggal (Single Source of Truth) untuk data film.
 *
 * Mengambil data dari MovieApi (Retrofit), memetakan DTO menjadi model domain,
 * dan menyediakan fungsi siap pakai untuk ViewModel.
 */
class MovieRepository(
    private val api: MovieApi = ApiClient.movieApi
) {

    /**
     * Mengambil seluruh daftar ringkasan film dari server.
     */
    suspend fun getMovies(): List<MovieSummary> {
        return api.getMovies().map { it.toDomain() }
    }

    /**
     * Mengambil detail lengkap satu film berdasarkan ID.
     */
    suspend fun getMovieDetail(id: Int): MovieDetail {
        return api.getMovieDetail(id).toDomain()
    }
}
