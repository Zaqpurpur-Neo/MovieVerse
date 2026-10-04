package com.verse.movieverse.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTO = bentuk JSON TMDB apa adanya.
 * Semua field nullable dengan default null agar data kosong tidak membuat aplikasi crash.
 */
data class TmdbMovieDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("popularity")
    val popularity: Double? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null
)

data class TmdbPageDto(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<TmdbMovieDto>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
)

data class GenreDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null
)

data class TmdbCastDto(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("character")
    val character: String? = null,

    @SerializedName("order")
    val order: Int? = null
)

data class TmdbCrewDto(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("job")
    val job: String? = null
)

data class TmdbCreditsDto(
    @SerializedName("cast")
    val cast: List<TmdbCastDto>? = null,

    @SerializedName("crew")
    val crew: List<TmdbCrewDto>? = null
)

data class TmdbVideoDto(
    @SerializedName("key")
    val key: String? = null,

    @SerializedName("site")
    val site: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("official")
    val official: Boolean? = null
)

data class TmdbVideosDto(
    @SerializedName("results")
    val results: List<TmdbVideoDto>? = null
)

data class TmdbDetailDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("popularity")
    val popularity: Double? = null,

    @SerializedName("runtime")
    val runtime: Int? = null,

    @SerializedName("genres")
    val genres: List<GenreDto>? = null,

    @SerializedName("credits")
    val credits: TmdbCreditsDto? = null,

    @SerializedName("videos")
    val videos: TmdbVideosDto? = null
)