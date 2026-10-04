package com.verse.movieverse.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.ui.components.PosterImage
import java.io.File

@Composable
fun AkunScreen(
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: AkunViewModel = viewModel(factory = AkunViewModel.Factory(context))
    val nama by viewModel.nama.collectAsStateWithLifecycle()
    val photoPath by viewModel.photoPath.collectAsStateWithLifecycle()

    // State untuk statistik (Fase 7C)
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderProfil(
            nama = nama,
            photoPath = photoPath,
            onEditClick = { showEditDialog = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatistikSection(
            filmDinilai = viewModel.jumlahFilmDinilai(),
            ulasanDitulis = viewModel.jumlahUlasanDitulis(),
            watchlist = viewModel.jumlahWatchlist(),
            sudahDitonton = viewModel.jumlahSudahDitonton()
        )

        FilmFavoritSection(
            favorites = viewModel.filmFavorit(),
            onOpenDetail = onOpenDetail
        )

        DistribusiRatingSection(
            distribusi = viewModel.distribusiRating(),
            rataRata = viewModel.rataRataRating(),
            totalUlasan = reviews.size
        )

        SectionPengaturan(
            onResetClick = { showResetDialog = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTentang()

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showEditDialog) {
        DialogEditProfil(
            namaAwal = nama,
            onDismiss = { showEditDialog = false },
            onSimpan = { namaBaru ->
                viewModel.onSaveName(namaBaru)
                showEditDialog = false
            },
            onGantiFoto = { uri ->
                viewModel.onPhotoPicked(uri)
            }
        )
    }

    if (showResetDialog) {
        DialogResetData(
            onDismiss = { showResetDialog = false },
            onKonfirmasi = {
                viewModel.onResetAll()
                showResetDialog = false
            }
        )
    }
}

@Composable
private fun HeaderProfil(
    nama: String,
    photoPath: String?,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageModel = if (photoPath != null) File(photoPath) else null
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nama,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Koleksi Sinema Offline",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onEditClick) {
            Text("Edit Profil")
        }
    }
}

@Composable
private fun DialogEditProfil(
    namaAwal: String,
    onDismiss: () -> Unit,
    onSimpan: (String) -> Unit,
    onGantiFoto: (android.net.Uri) -> Unit
) {
    var namaInput by rememberSaveable { mutableStateOf(namaAwal) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onGantiFoto(uri)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profil") },
        text = {
            Column {
                OutlinedTextField(
                    value = namaInput,
                    onValueChange = { if (it.length <= 30) namaInput = it },
                    label = { Text("Nama") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ganti Foto")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSimpan(namaInput) },
                enabled = namaInput.trim().isNotEmpty()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// === Komponen Statistik Fase 7C ===

@Composable
private fun StatistikSection(
    filmDinilai: Int,
    ulasanDitulis: Int,
    watchlist: Int,
    sudahDitonton: Int
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Statistik Personal",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KartuStatistik("Film Dinilai", filmDinilai.toString(), Modifier.weight(1f))
            KartuStatistik("Ulasan Ditulis", ulasanDitulis.toString(), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KartuStatistik("Watchlist", watchlist.toString(), Modifier.weight(1f))
            KartuStatistik("Sudah Ditonton", sudahDitonton.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
private fun KartuStatistik(label: String, nilai: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
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

@Composable
private fun FilmFavoritSection(
    favorites: List<ReviewEntity>,
    onOpenDetail: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = "Film Favorit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "4 film penentu selera sinema",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (favorites.isEmpty()) {
            Text(
                text = "Belum ada favorit. Beri rating film dulu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        } else {
            // Materi Lazy: key wajib agar item dikenali dari id, bukan posisi
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.movieId }) { review ->
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable { onOpenDetail(review.movieId) },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PosterImage(
                            url = review.posterUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f / 3f)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = review.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DistribusiRatingSection(
    distribusi: Map<Int, Int>,
    rataRata: Float,
    totalUlasan: Int
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Distribusi Rating",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Rata-rata ${String.format("%.1f", rataRata).replace('.', ',')}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Tampilkan dari bintang 5 sampai 1
        for (bintang in 5 downTo 1) {
            val jumlah = distribusi[bintang] ?: 0
            val progress = if (totalUlasan > 0) jumlah.toFloat() / totalUlasan.toFloat() else 0f

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$bintang ★",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(32.dp)
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.small),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                Text(
                    text = jumlah.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// === Pengaturan & Tentang (Fase 7B) ===

@Composable
private fun SectionPengaturan(onResetClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column {
                BarisPengaturan(
                    judul = "Tema",
                    deskripsi = "Gelap (Ungu Pastel M3)",
                    onClick = { /* Statis, tidak ada aksi */ }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                BarisPengaturan(
                    judul = "Reset Data Lokal",
                    deskripsi = "Hapus database dan reset ke pengaturan awal",
                    isDestructive = true,
                    onClick = onResetClick
                )
            }
        }
    }
}

@Composable
private fun BarisPengaturan(
    judul: String,
    deskripsi: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = judul,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = deskripsi,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DialogResetData(
    onDismiss: () -> Unit,
    onKonfirmasi: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reset Data Lokal?") },
        text = { Text("Semua ulasan, watchlist, riwayat tontonan, dan riwayat pencarian akan dihapus permanen dari perangkat ini.") },
        confirmButton = {
            TextButton(onClick = onKonfirmasi) {
                Text("Ya, Hapus Semua", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
private fun SectionTentang() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Tentang",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MovieVerse v1.0.0",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Data film bersifat demo. Aplikasi ini tidak terhubung ke server TMDB secara langsung saat runtime.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Atribusi:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Poster dimuat dari TMDB (image.tmdb.org)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "• Trailer dari MovieLens 20M YouTube Trailers (GroupLens, CC BY 4.0)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "• Data film dari The Movies Dataset (Kaggle)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}