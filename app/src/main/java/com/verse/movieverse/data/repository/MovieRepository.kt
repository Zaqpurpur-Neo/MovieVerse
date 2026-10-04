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
    }

    /**
     * Sementara: semua layar yang masih memakai getMovies() akan mendapat
     * film populer halaman 1. Nanti diganti per layar sesuai kebutuhan.
     */
    suspend fun getMovies(): List<MovieSummary> {
        return getPopular(genre = null, page = 1).movies
    }

    suspend fun getPopular(genre: String?, page: Int): MoviePage {
        val dto = api.discover(
            genres = genreIdOrNull(genre),
            sortBy = "popularity.desc",
            minVotes = 100,
            releaseLte = null,
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

    suspend fun getTopRated(page: Int): MoviePage {
        val dto = api.discover(
            genres = null,
            sortBy = "vote_average.desc",
            minVotes = 2000,
            releaseLte = null,
            page = page,
            language = LANGUAGE,
            includeAdult = false
        )
        return toMoviePage(dto)
    }

    suspend fun getClassics(page: Int): MoviePage {
        val dto = api.discover(
            genres = null,
            sortBy = "vote_average.desc",
            minVotes = 1000,
            releaseLte = "1989-12-31",
            page = page,
            language = LANGUAGE,
            includeAdult = false
        )
        return toMoviePage(dto)
    }

    suspend fun getByGenre(genre: String, page: Int): MoviePage {
        val dto = api.discover(
            genres = GenreMap.idDariNama(genre)?.toString(),
            sortBy = "popularity.desc",
            minVotes = 50,
            releaseLte = null,
            page = page,
            language = LANGUAGE,
            includeAdult = false
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

    private fun genreIdOrNull(genre: String?): String? {
        if (genre == null || genre == "Semua") return null
        return GenreMap.idDariNama(genre)?.toString()
    }
}