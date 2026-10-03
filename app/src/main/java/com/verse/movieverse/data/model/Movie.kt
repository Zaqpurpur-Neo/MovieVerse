package com.verse.movieverse.data.model

import com.verse.movieverse.data.remote.CastDto
import com.verse.movieverse.data.remote.MovieDetailDto
import com.verse.movieverse.data.remote.MovieSummaryDto

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
 * Helper untuk membentuk URL poster lengkap dari path TMDb.
 */
fun formatPosterUrl(path: String?): String {
    return if (!path.isNullOrBlank()) {
        "https://image.tmdb.org/t/p/w500$path"
    } else {
        ""
    }
}

/**
 * Fungsi ekstensi untuk memetakan MovieSummaryDto ke model domain MovieSummary.
 */
fun MovieSummaryDto.toDomain(): MovieSummary {
    return MovieSummary(
        id = id ?: 0,
        title = title.orEmpty(),
        year = year ?: 0,
        releaseDate = releaseDate.orEmpty(),
        genres = genres.orEmpty(),
        rating = rating ?: 0.0,
        popularity = popularity ?: 0.0,
        posterUrl = formatPosterUrl(poster),
        overview = overview.orEmpty(),
        director = director.orEmpty(),
        cast = cast.orEmpty(),
        trailerId = trailerId
    )
}

/**
 * Fungsi ekstensi untuk memetakan CastDto ke model domain CastMember.
 */
fun CastDto.toDomain(): CastMember {
    return CastMember(
        name = name.orEmpty(),
        character = character.orEmpty()
    )
}

/**
 * Fungsi ekstensi untuk memetakan MovieDetailDto ke model domain MovieDetail.
 */
fun MovieDetailDto.toDomain(): MovieDetail {
    return MovieDetail(
        id = id ?: 0,
        title = title.orEmpty(),
        year = year ?: 0,
        releaseDate = releaseDate.orEmpty(),
        genres = genres.orEmpty(),
        rating = rating ?: 0.0,
        popularity = popularity ?: 0.0,
        posterUrl = formatPosterUrl(poster),
        overview = overview.orEmpty(),
        director = director.orEmpty(),
        writers = writers.orEmpty(),
        runtime = runtime,
        cast = cast?.map { it.toDomain() }.orEmpty(),
        trailerId = trailerId
    )
}
