package com.verse.movieverse.data.model

import com.verse.movieverse.data.remote.TmdbDetailDto
import com.verse.movieverse.data.remote.TmdbMovieDto
import kotlin.math.roundToInt

/**
 * Model domain ringkasan film untuk tampilan daftar katalog (UI).
 */
data class MovieSummary(
    val id: Int,
    val title: String,
    val year: Int,
    val releaseDate: String,
    val genres: List<String>,
    val rating: Double,
    val popularity: Double,
    val posterUrl: String,
    val overview: String,
    val director: String,
    val cast: List<String>,
    val trailerId: String?
)

/**
 * Model domain anggota pemeran film.
 */
data class CastMember(
    val name: String,
    val character: String
)

/**
 * Model domain detail film lengkap untuk layar Detail Film.
 */
data class MovieDetail(
    val id: Int,
    val title: String,
    val year: Int,
    val releaseDate: String,
    val genres: List<String>,
    val rating: Double,
    val popularity: Double,
    val posterUrl: String,
    val overview: String,
    val director: String,
    val writers: List<String>,
    val runtime: Int?,
    val cast: List<CastMember>,
    val trailerId: String?
)

/**
 * Helper untuk membentuk URL poster lengkap dari path TMDB.
 */
fun formatPosterUrl(path: String?): String {
    return if (!path.isNullOrBlank()) {
        "https://image.tmdb.org/t/p/w500$path"
    } else {
        ""
    }
}

/**
 * Bulatkan rating ke 1 angka desimal, misal 8.67 menjadi 8.7.
 */
private fun Double?.roundOneDecimal(): Double {
    val value = this ?: 0.0
    return (value * 10).roundToInt() / 10.0
}

/**
 * Mapper dari DTO daftar film TMDB ke model domain MovieSummary.
 * Mengembalikan null bila id atau title tidak ada, agar item rusak tidak ditampilkan.
 */
fun TmdbMovieDto.toSummary(): MovieSummary? {
    val movieId = id ?: return null
    val movieTitle = title?.takeIf { it.isNotBlank() } ?: return null

    return MovieSummary(
        id = movieId,
        title = movieTitle,
        year = releaseDate?.take(4)?.toIntOrNull() ?: 0,
        releaseDate = releaseDate.orEmpty(),
        genres = genreIds?.mapNotNull { GenreMap.namaDariId(it) }.orEmpty(),
        rating = voteAverage.roundOneDecimal(),
        popularity = popularity ?: 0.0,
        posterUrl = formatPosterUrl(posterPath),
        overview = overview.orEmpty(),
        director = "",
        cast = emptyList(),
        trailerId = null
    )
}

/**
 * Mapper dari DTO detail film TMDB ke model domain MovieDetail.
 */
fun TmdbDetailDto.toDetail(): MovieDetail {
    val writerJobs = setOf("Screenplay", "Writer", "Story")

    return MovieDetail(
        id = id ?: 0,
        title = title.orEmpty(),
        year = releaseDate?.take(4)?.toIntOrNull() ?: 0,
        releaseDate = releaseDate.orEmpty(),
        genres = genres
            ?.mapNotNull { genreDto ->
                GenreMap.namaDariId(genreDto.id ?: -1) ?: genreDto.name
            }
            .orEmpty(),
        rating = voteAverage.roundOneDecimal(),
        popularity = popularity ?: 0.0,
        posterUrl = formatPosterUrl(posterPath),
        overview = overview.orEmpty(),
        director = credits?.crew
            ?.firstOrNull { it.job == "Director" }
            ?.name
            .orEmpty(),
        writers = credits?.crew
            ?.filter { it.job in writerJobs }
            ?.mapNotNull { it.name }
            ?.distinct()
            ?.take(2)
            .orEmpty(),
        runtime = runtime,
        cast = credits?.cast
            ?.sortedBy { it.order ?: Int.MAX_VALUE }
            ?.take(5)
            ?.map { castDto ->
                CastMember(
                    name = castDto.name.orEmpty(),
                    character = castDto.character.orEmpty()
                )
            }
            .orEmpty(),
        trailerId = videos?.results
            ?.filter { videoDto ->
                videoDto.site == "YouTube" &&
                        videoDto.type == "Trailer" &&
                        !videoDto.key.isNullOrBlank()
            }
            ?.sortedByDescending { it.official == true }
            ?.firstOrNull()
            ?.key
    )
}