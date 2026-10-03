package com.verse.movieverse.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * Komponen gambar poster film.
 * Coil memuat gambar dari URL; jika gagal tampil kotak polos warna surfaceVariant.
 */
@Composable
fun PosterImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val fallback = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = fallback,
        error = fallback,
        modifier = modifier
    )
}
