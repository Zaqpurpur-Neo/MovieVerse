package com.verse.movieverse.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * Bottom sheet "Tulis Ulasan" dan "Edit Ulasan" dalam SATU composable.
 * STATELESS (state hoisting): nilai awal diterima dari parent, hasil
 * dikembalikan lewat lambda onSimpan / onHapus / onTutup.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewSheet(
    modeEdit: Boolean,
    judul: String,
    tahun: Int,
    posterUrl: String,
    ratingAwal: Float,
    catatanAwal: String,
    tanggalAwal: String,
    isRewatchAwal: Boolean,
    onSimpan: (rating: Float, tanggal: String, isRewatch: Boolean, catatan: String) -> Unit,
    onHapus: () -> Unit,
    onTutup: () -> Unit
) {
    // Materi State: rememberSaveable -> isian bertahan saat layar diputar
    // selama sheet masih terbuka.
    var rating by rememberSaveable { mutableStateOf(ratingAwal) }
    var catatan by rememberSaveable { mutableStateOf(catatanAwal) }
    var isRewatch by rememberSaveable { mutableStateOf(isRewatchAwal) }

    // Rating dianggap "terisi" bila sudah >= 0,5
    val ratingTerisi = rating >= 0.5f

    ModalBottomSheet(onDismissRequest = onTutup) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header: poster mini + judul + tahun
            Row(verticalAlignment = Alignment.CenterVertically) {
                PosterImage(
                    url = posterUrl,
                    modifier = Modifier
                        .width(48.dp)
                        .height(72.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (modeEdit) "Edit Ulasan" else "Tulis Ulasan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$judul • $tahun",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rating: Slider 0,5 sampai 5 dengan langkah 0,5
            Text(
                text = "Rating Anda",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = rating.coerceAtLeast(0.5f),
                onValueChange = { rating = it },
                valueRange = 0.5f..5f,
                steps = 8, // 8 titik perhentian -> langkah 0,5
                modifier = Modifier.fillMaxWidth()
            )
            BarisBintang(rating = rating)

            Spacer(modifier = Modifier.height(12.dp))

            // Tanggal menonton: teks saja, TANPA date picker (disederhanakan)
            Text(
                text = "Tanggal Menonton",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = tanggalAwal,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Switch tonton ulang
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tonton Ulang",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Tandai jika Anda menonton film ini lagi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = isRewatch, onCheckedChange = { isRewatch = it })
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Catatan multibaris
            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text("Catatan Anda") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol aksi utama
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onTutup,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
                // Nonaktif bila rating belum diisi
                Button(
                    onClick = { onSimpan(rating, tanggalAwal, isRewatch, catatan) },
                    enabled = ratingTerisi,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Simpan Ulasan")
                }
            }

            // Mode edit menambah tombol hapus
            if (modeEdit) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onHapus,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hapus Ulasan")
                }
            }
        }
    }
}

/**
 * Baris 5 bintang sesuai nilai rating (mendukung setengah bintang).
 */
@Composable
fun BarisBintang(
    rating: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val ikon = when {
                rating >= i -> Icons.Filled.Star
                rating >= i - 0.5f -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            Icon(
                imageVector = ikon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format(Locale.US, "%.1f / 5.0", rating),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}