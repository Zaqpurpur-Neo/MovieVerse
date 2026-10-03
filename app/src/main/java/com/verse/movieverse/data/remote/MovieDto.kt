package com.verse.movieverse.data.remote

/**
 * Data Transfer Object (DTO) untuk serialisasi/deserialisasi JSON Retrofit (Gson).
 *
 * DTO merefleksikan skema JSON apa adanya dari respons jaringan.
 * Seluruh field dibuat bertipe nullable dengan nilai default null agar bila terdapat
 * data kosong atau field yang hilang di JSON, parsing data tidak menyebabkan crash (null-safety).
 */

data class MovieSummaryDto(
    val id: Int? = null,
    val title: String? = null,
    val year: Int? = null,
    val releaseDate: String? = null,
    val genres: List<String>? = null,
    val rating: Double? = null,
    val popularity: Double? = null,
    val poster: String? = null,
    val overview: String? = null,
    val director: String? = null,
    val cast: List<String>? = null,
    val trailerId: String? = null
)

data class CastDto(
    val name: String? = null,
    val character: String? = null
)

data class MovieDetailDto(
    val id: Int? = null,
    val title: String? = null,
    val year: Int? = null,
    val releaseDate: String? = null,
    val genres: List<String>? = null,
    val rating: Double? = null,
    val popularity: Double? = null,
    val poster: String? = null,
    val overview: String? = null,
    val director: String? = null,
    val writers: List<String>? = null,
    val runtime: Int? = null,
    val cast: List<CastDto>? = null,
    val trailerId: String? = null
)
