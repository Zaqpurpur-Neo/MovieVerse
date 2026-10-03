package com.verse.movieverse.navigation

import kotlinx.serialization.Serializable

/**
 * Definisi rute navigasi menggunakan Kotlin Serialization (@Serializable).
 *
 * Mengapa @Serializable?
 * Navigation Compose type-safe menggunakan anotasi ini untuk menghasilkan rute
 * secara otomatis dan aman saat kompilasi (compile-time safety) tanpa perlu membuat
 * pola string URL manual yang rawan kesalahan ketik (typo).
 *
 * Bagaimana data dikirim lewat konstruktor rute?
 * Data class (seperti HasilPencarian dan DetailFilm) mendefinisikan parameter pada konstruktornya.
 * Saat berpindah layar (misal: navController.navigate(DetailFilm(movieId = 123))), Navigation Compose
 * mengemas parameter tersebut ke dalam Bundle rute dan diekstrak kembali pada layar tujuan
 * menggunakan backStackEntry.toRoute<DetailFilm>().
 */

// Rute untuk 4 tab utama
@Serializable
object Jelajah

@Serializable
object Kategori

@Serializable
object Jurnal

@Serializable
object Akun

// Rute untuk layar non-tab
@Serializable
object PencarianAktif

@Serializable
data class HasilPencarian(val query: String)

@Serializable
data class DetailFilm(val movieId: Int)
