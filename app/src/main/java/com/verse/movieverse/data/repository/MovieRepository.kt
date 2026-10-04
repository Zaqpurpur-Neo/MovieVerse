package com.verse.movieverse.data.repository

import com.verse.movieverse.data.model.GenreMap
import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.model.toDetail
import com.verse.movieverse.data.model.toSummary
import com.verse.movieverse.data.remote.ApiClient
import com.verse.movieverse.data.remote.MovieApi
import com.verse.movieverse.data.remote.TmdbPageDto

/**
 * Hasil satu halaman film dari TMDB.
 */
data class MoviePage(
    val movies: List<MovieSummary>,
    val page: Int,
    val totalPages: Int,
    val totalResults: Int
)

/**
 * Repository = satu-satunya pintu data untuk ViewModel.
 * Repository memanggil MovieApi, lalu memetakan DTO TMDB ke model domain.
 */
class MovieRepository(
    private val api: MovieApi = ApiClient.movieApi
) {

    private companion object {
        const val LANGUAGE = "en-US"

        // Batas minimum suara agar film sampah tidak masuk daftar.
        const val MIN_VOTES = 50
    }

    /**
     * Satu fungsi untuk semua kebutuhan discover: Jelajah, kategori genre,
     * dan rentang tahun. Parameter defaulted, jadi pemanggil cukup
     * menyebut yang berbeda.
     */
    suspend fun discover(
        genre: String? = null,
        sortBy: String = "popularity.desc",
        minVotes: Int = MIN_VOTES,
        releaseLte: String? = null,
        page: Int = 1
    ): MoviePage {
        val dto = api.discover(
            genres = genre?.let { GenreMap.idDariNama(it)?.toString() },
            sortBy = sortBy,
            minVotes = minVotes,
            releaseLte = releaseLte,
            page = page,
            language = LANGUAGE,
            includeAdult = false
        )
        return toMoviePage(dto)
    }

    suspend fun getNowPlaying(page: Int): MoviePage {
        val dto = api.nowPlaying(
            page = page,
            language = LANGUAGE
        )
        return toMoviePage(dto)
    }

    suspend fun search(query: String, page: Int): MoviePage {
        val dto = api.searchMovie(
            query = query,
            page = page,
            language = LANGUAGE,
            includeAdult = false
        )
        return toMoviePage(dto)
    }

    suspend fun getMovieDetail(id: Int): MovieDetail {
        val dto = api.movieDetail(
            id = id,
            append = "credits,videos",
            language = LANGUAGE
        )
        val detail = dto.toDetail()

        if (detail.title.isBlank()) {
            throw IllegalStateException("Detail film tidak ditemukan.")
        }

        return detail
    }

    private fun toMoviePage(dto: TmdbPageDto): MoviePage {
        val movies = dto.results
            ?.mapNotNull { it.toSummary() }
            .orEmpty()

        return MoviePage(
            movies = movies,
            page = dto.page ?: 1,
            totalPages = dto.totalPages ?: 1,
            totalResults = dto.totalResults ?: movies.size
        )
    }
}