package com.verse.movieverse.ui.common

import java.util.Locale

/**
 * Format angka 1 desimal dengan pemisah koma Indonesia, mis. "4,5".
 * Satu fungsi dipakai semua layar agar format rating konsisten.
 */
fun formatSkor(nilai: Double): String =
    String.format(Locale("id", "ID"), "%.1f", nilai)