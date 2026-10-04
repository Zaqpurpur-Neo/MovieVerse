package com.verse.movieverse.data.model

/**
 * Peta tetap id genre TMDB ke nama genre berbahasa Indonesia.
 */
object GenreMap {

    private val peta: Map<Int, String> = mapOf(
        28 to "Aksi",
        12 to "Petualangan",
        16 to "Animasi",
        35 to "Komedi",
        80 to "Kriminal",
        99 to "Dokumenter",
        18 to "Drama",
        10751 to "Keluarga",
        14 to "Fantasi",
        36 to "Sejarah",
        27 to "Horor",
        10402 to "Musik",
        9648 to "Misteri",
        10749 to "Romansa",
        878 to "Sci-Fi",
        10770 to "Film TV",
        53 to "Thriller",
        10752 to "Perang",
        37 to "Western"
    )

    fun namaDariId(id: Int): String? {
        return peta[id]
    }

    fun idDariNama(nama: String): Int? {
        return peta.entries
            .firstOrNull { it.value.equals(nama, ignoreCase = true) }
            ?.key
    }
}