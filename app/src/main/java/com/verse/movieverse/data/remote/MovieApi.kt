package com.verse.movieverse.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface Retrofit untuk mendefinisikan endpoint API data film.
 */
interface MovieApi {

    @GET("movies.json")
    suspend fun getMovies(): List<MovieSummaryDto>

    @GET("movies/{id}.json")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): MovieDetailDto
}

/**
 * Singleton Retrofit client untuk membuat instance MovieApi.
 */
object ApiClient {
    const val BASE_URL = "https://zaqpurpur-neo.github.io/movieverse-data/"

    val movieApi: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
}
