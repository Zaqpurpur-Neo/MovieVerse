package com.verse.movieverse.ui.common

import android.content.Context
import android.content.Intent

/**
 * Materi Intent: ACTION_SEND membuka dialog berbagi sistem.
 * Satu helper dipakai semua layar yang punya tombol Bagikan.
 */
fun bagikanTeks(context: Context, teks: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, teks)
    }
    context.startActivity(Intent.createChooser(intent, "Bagikan via"))
}