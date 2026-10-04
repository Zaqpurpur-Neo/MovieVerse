package com.verse.movieverse.ui.common

import com.verse.movieverse.data.local.ReviewEntity
import kotlin.math.roundToInt

/**
 * Perhitungan statistik pribadi.
 * Semua ditulis sebagai fungsi ekstensi atas daftar ulasan supaya
 * Jurnal dan Akun memakai rumus yang sama tanpa menyalinnya.
 */

/** Jumlah ulasan yang sudah memberi rating, mis. "Film Dinilai". */
fun List<ReviewEntity>.jumlahFilmDinilai(): Int = size

/** Jumlah ulasan yang catatannya tidak kosong. */
fun List<ReviewEntity>.jumlahUlasanDitulis(): Int = count { it.note.isNotBlank() }

/** Rata-rata skor, 0 bila belum ada ulasan. */
fun List<ReviewEntity>.rataRataSkor(): Float {
    if (isEmpty()) return 0f
    return (sumOf { it.rating.toDouble() } / size).toFloat()
}

/** Distribusi bintang 1 sampai 5. Rating dibulatkan ke bilangan bulat terdekat. */
fun List<ReviewEntity>.distribusiRating(): Map<Int, Int> {
    val hasil = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
    forEach { review ->
        val bintang = review.rating.roundToInt().coerceIn(1, 5)
        hasil[bintang] = (hasil[bintang] ?: 0) + 1
    }
    return hasil
}

/** Empat ulasan rating tertinggi. Seri: yang dibuat lebih baru. */
fun List<ReviewEntity>.filmFavorit(): List<ReviewEntity> =
    sortedWith(compareByDescending<ReviewEntity> { it.rating }.thenByDescending { it.createdAt })
        .take(4)