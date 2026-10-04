package com.verse.movieverse.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Kartu angka statistik pribadi, mis. "12 / Film Dinilai".
 */
@Composable
fun KartuStatistik(label: String, nilai: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nilai,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Grid 2x2 kartu statistik. Dipakai layar Jurnal dan Akun dengan isi berbeda,
 * jadi cukup satu komponen untuk keduanya.
 */
@Composable
fun StatistikSection(items: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        items.chunked(2).forEach { baris ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                baris.forEach { (label, nilai) ->
                    KartuStatistik(
                        label = label,
                        nilai = nilai,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Baris ganjil: isi ruang kosong agar kartu tetap sama lebar.
                repeat(2 - baris.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}