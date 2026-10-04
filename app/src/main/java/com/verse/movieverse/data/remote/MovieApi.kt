package com.verse.movieverse.data.remote

import com.verse.movieverse.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
/**
 * Interface Retrofit untuk TMDB API v3.
 * Semua fungsi suspend agar dipakai dari coroutine ViewModel.
 */
interface MovieApi {

    @GET("discover/movie")
    suspend fun discover(
        @Query("with_genres") genres: String?,
        @Query("sort_by") sortBy: String,
        @Query("vote_count.gte") minVotes: Int?,
        @Query("primary_release_date.lte") releaseLte: String?,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("include_adult") includeAdult: Boolean
    ): TmdbPageDto

    @GET("movie/now_playing")
    suspend fun nowPlaying(
        @Query("page") page: Int,
        @Query("language") language: String
    ): TmdbPageDto

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("include_adult") includeAdult: Boolean
    ): TmdbPageDto

    @GET("movie/{id}")
    suspend fun movieDetail(
        @Path("id") id: Int,
        @Query("append_to_response") append: String,
        @Query("language") language: String
    ): TmdbDetailDto
}

/**
 * ApiClient membuat satu instance Retrofit.
 * Interceptor menambahkan token otorisasi ke setiap permintaan.
 */
object ApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
                    .addHeader("accept", "application/json")
                    .build()
                return chain.proceed(request)
            }
        })
        .build()

    val movieApi: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
}