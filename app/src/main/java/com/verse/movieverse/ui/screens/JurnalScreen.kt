package com.verse.movieverse.ui.screens

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.data.local.WatchedEntity
import com.verse.movieverse.data.local.WatchlistEntity
import com.verse.movieverse.ui.common.bagikanTeks
import com.verse.movieverse.ui.common.formatSkor
import com.verse.movieverse.ui.common.jumlahFilmDinilai
import com.verse.movieverse.ui.common.jumlahUlasanDitulis
import com.verse.movieverse.ui.common.rataRataSkor
import com.verse.movieverse.ui.components.PosterImage
import com.verse.movieverse.ui.components.StatistikSection

@Composable
fun JurnalScreen(
    onOpenDetail: (Int) -> Unit,
    onOpenSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: JurnalViewModel = viewModel(factory = JurnalViewModel.factory(context))
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()
    val watched by viewModel.watched.collectAsStateWithLifecycle()

    // Materi State: rememberSaveable menahan tab terpilih saat layar diputar
    var tabAktif by rememberSaveable { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Jurnal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Catatan apresiasi & ulasan film pribadi • tersimpan di perangkat",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Empat kartu statistik
            StatistikSection(
                items = listOf(
                    "Film Dinilai" to reviews.jumlahFilmDinilai().toString(),
                    "Ulasan Ditulis" to reviews.jumlahUlasanDitulis().toString(),
                    "Rata-rata Skor" to formatSkor(reviews.rataRataSkor().toDouble()),
                    "Watchlist" to watchlist.size.toString()
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TabRow: komponen M3 yang stabil, tanpa @OptIn
            TabRow(selectedTabIndex = tabAktif) {
                listOf("Ulasan Saya", "Watchlist", "Sudah Ditonton").forEachIndexed { index, judul ->
                    Tab(
                        selected = tabAktif == index,
                        onClick = { tabAktif = index },
                        text = { Text(judul) }
                    )
                }
            }

            when (tabAktif) {
                0 -> DaftarUlasan(
                    ulasan = reviews,
                    onOpenDetail = onOpenDetail,
                    onBagikan = { review -> bagikanUlasan(context, review) }
                )
                1 -> DaftarWatchlist(
                    daftar = watchlist,
                    onOpenDetail = onOpenDetail,
                    onMarkWatched = { viewModel.onMarkWatched(it) },
                    onHapus = { viewModel.onRemoveFromWatchlist(it) }
                )
                else -> DaftarWatched(
                    daftar = watched,
                    onOpenDetail = onOpenDetail
                )
            }
        }

        // Tombol melayang "Catat Film" -> layar Pencarian Aktif
        ExtendedFloatingActionButton(
            onClick = onOpenSearch,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Catat Film") },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun DaftarUlasan(
    ulasan: List<ReviewEntity>,
    onOpenDetail: (Int) -> Unit,
    onBagikan: (ReviewEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (ulasan.isEmpty()) {
            item {
                TeksKosong("Belum ada ulasan. Ketuk \"Catat Film\" untuk memulai.")
            }
        } else {
            // Materi Lazy: key wajib agar item dikenali dari id, bukan posisi
            items(ulasan, key = { it.movieId }) { review ->
                KartuUlasan(
                    review = review,
                    onClick = { onOpenDetail(review.movieId) },
                    onBagikan = { onBagikan(review) }
                )
            }
        }
    }
}

// Kartu ulasan: stateless, menerima data + lambda (state hoisting)
@Composable
private fun KartuUlasan(
    review: ReviewEntity,
    onClick: () -> Unit,
    onBagikan: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                PosterImage(
                    url = review.posterUrl,
                    modifier = Modifier
                        .width(56.dp)
                        .aspectRatio(2f / 3f)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${review.year} • ${review.watchedDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "★ ${formatSkor(review.rating.toDouble())}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    if (review.isRewatch) {
                        Text(
                            text = "Tonton Ulang",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (review.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                TextButton(onClick = onClick) {
                    Text("Edit")
                }
                TextButton(onClick = onBagikan) {
                    Text("Bagikan")
                }
            }
        }
    }
}

@Composable
private fun DaftarWatchlist(
    daftar: List<WatchlistEntity>,
    onOpenDetail: (Int) -> Unit,
    onMarkWatched: (WatchlistEntity) -> Unit,
    onHapus: (WatchlistEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (daftar.isEmpty()) {
            item {
                TeksKosong("Watchlist Anda masih kosong.")
            }
        } else {
            // Materi Lazy: key wajib agar item dikenali dari id, bukan posisi
            items(daftar, key = { it.movieId }) { item ->
                KartuWatchlist(
                    item = item,
                    onClick = { onOpenDetail(item.movieId) },
                    onMarkWatched = { onMarkWatched(item) },
                    onHapus = { onHapus(item) }
                )
            }
        }
    }
}

// Kartu watchlist: stateless, menerima data + lambda (state hoisting)
@Composable
private fun KartuWatchlist(
    item: WatchlistEntity,
    onClick: () -> Unit,
    onMarkWatched: () -> Unit,
    onHapus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                PosterImage(
                    url = item.posterUrl,
                    modifier = Modifier
                        .width(56.dp)
                        .aspectRatio(2f / 3f)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.year.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                TextButton(
                    onClick = onMarkWatched,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tandai Sudah Ditonton")
                }
                TextButton(
                    onClick = onHapus,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}

@Composable
private fun DaftarWatched(
    daftar: List<WatchedEntity>,
    onOpenDetail: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (daftar.isEmpty()) {
            item {
                TeksKosong("Belum ada film yang ditandai selesai ditonton.")
            }
        } else {
            // Materi Lazy: key wajib agar item dikenali dari id, bukan posisi
            items(daftar, key = { it.movieId }) { item ->
                KartuWatched(
                    item = item,
                    onClick = { onOpenDetail(item.movieId) }
                )
            }
        }
    }
}

// Kartu sudah ditonton: sederhana, stateless
@Composable
private fun KartuWatched(
    item: WatchedEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            PosterImage(
                url = item.posterUrl,
                modifier = Modifier
                    .width(56.dp)
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.year} • selesai ditonton",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TeksKosong(pesan: String) {
    Text(
        text = pesan,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
    )
}

// Materi Intent: ACTION_SEND membuka dialog berbagi sistem
private fun bagikanUlasan(context: Context, review: ReviewEntity) {
    val teks = "Saya memberi ${review.title} ${formatSkor(review.rating.toDouble())}/5 di MovieVerse"
    bagikanTeks(context, teks)
}