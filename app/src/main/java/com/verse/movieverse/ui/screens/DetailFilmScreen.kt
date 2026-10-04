package com.verse.movieverse.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.verse.movieverse.data.model.CastMember
import com.verse.movieverse.data.model.MovieDetail
import com.verse.movieverse.ui.common.UiState
import com.verse.movieverse.ui.common.bagikanTeks
import com.verse.movieverse.ui.components.BagianMuat
import com.verse.movieverse.ui.components.BarisBintang
import com.verse.movieverse.ui.components.PosterImage
import com.verse.movieverse.ui.components.ReviewSheet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailFilmScreen(
    movieId: Int,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // ViewModel dibuat lewat Factory sederhana (materi: injeksi dependensi tanpa DI framework)
    val viewModel: DetailFilmViewModel = viewModel(factory = DetailFilmViewModel.factory(context))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // State hoisting: status tombol dibaca dari ViewModel, tombol hanya menerima nilai + lambda
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val isWatched by viewModel.isWatched.collectAsStateWithLifecycle()
    val myReview by viewModel.myReview.collectAsStateWithLifecycle()
    val showSheet by viewModel.showSheet.collectAsStateWithLifecycle()

    // Snackbar untuk feedback aksi Simpan
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope untuk menjalankan snackbar dari listener klik
    val scope = rememberCoroutineScope()

    // LaunchedEffect dipicu setiap movieId berubah (state hoisting dari navigasi)
    LaunchedEffect(movieId) {
        viewModel.muat(movieId)
    }

    // Scaffold di sini HANYA rumah untuk Snackbar.
    // paddingValues sengaja TIDAK dipakai: Scaffold luar (AppNavigation) sudah
    // memberi padding status bar & bottom bar. Memakainya lagi akan menambah
    // tinggi status bar dua kali -> muncul gap kosong di atas poster.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        when (val state = uiState) {
            // Loading dan Error ditangani satu komponen yang sama dengan layar lain.
            // Kembali tetap bisa lewat tombol sistem Android.
            is UiState.Loading, is UiState.Error -> BagianMuat(
                state = state,
                modifier = modifier.fillMaxSize(),
                onRetry = { viewModel.retry() }
            ) { }

            is UiState.Success -> {
                val film = state.data
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // a. Hero Poster + Gradien + Top Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        PosterImage(
                            url = film.posterUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradien gelap vertikal di bawah agar teks terbaca
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        // Top bar overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // State hoisting: aksi kembali dilempar ke parent
                            IconButton(onClick = onNavigateUp) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White
                                )
                            }
                            Text(
                                text = "Detail Film",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
IconButton(onClick = {
                                    bagikanTeks(
                                        context,
                                        "Tonton ${film.title} (${film.year}) - rating ${film.rating}/10"
                                    )
                                }) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Bagikan",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Konten detail di bawah hero
                    Column(modifier = Modifier.padding(16.dp)) {
                        // b. Judul + Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = film.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                ),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${film.rating} / 10",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Metadata: tahun • durasi
                        val durasi = formatDurasi(film.runtime)
                        Text(
                            text = if (durasi.isNotEmpty()) "${film.year} • $durasi" else "${film.year}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Genre chips (non-klik)
                        if (film.genres.isNotEmpty()) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                film.genres.forEach { genre ->
                                    AssistChip(
                                        onClick = { /* non-klik */ },
                                        label = { Text(genre) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // c. Tombol aksi (stateless: menerima status + lambda)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TombolSimpan(
                                isSaved = isSaved,
                                onClick = {
                                    // Pesan diambil dari status SEBELUM toggle
                                    val pesan = if (isSaved) {
                                        "Dihapus dari watchlist"
                                    } else {
                                        "Ditambahkan ke watchlist"
                                    }
                                    viewModel.onToggleSave()
                                    // Snackbar dijalankan lewat coroutine, BUKAN LaunchedEffect
                                    scope.launch { snackbarHostState.showSnackbar(pesan) }
                                },
                                modifier = Modifier.weight(1f)
                            )
                            TombolSudahDitonton(
                                isWatched = isWatched,
                                onClick = { viewModel.onToggleWatched() },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.onOpenSheet() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (myReview != null) "Edit Ulasan Saya" else "Tulis Ulasan Saya")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // d. Section Trailer
                        SectionTrailer(film = film, context = context)

                        Spacer(modifier = Modifier.height(24.dp))

                        // e. Section Sinopsis
                        SectionSinopsis(film = film)

                        Spacer(modifier = Modifier.height(24.dp))

                        // f. Section Pemeran
                        if (film.cast.isNotEmpty()) {
                            SectionPemeran(cast = film.cast)
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // g. Section Ulasan Saya
                        SectionUlasanSaya(
                            review = myReview,
                            onEdit = { viewModel.onOpenSheet() }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Bottom sheet ulasan pribadi: mode tulis atau edit tergantung ada/tidaknya ulasan
        val stateTerkini = uiState
        if (showSheet && stateTerkini is UiState.Success) {
            val filmSheet = stateTerkini.data
            val review = myReview
            ReviewSheet(
                modeEdit = review != null,
                judul = filmSheet.title,
                tahun = filmSheet.year,
                posterUrl = filmSheet.posterUrl,
                ratingAwal = review?.rating ?: 0f,
                catatanAwal = review?.note ?: "",
                tanggalAwal = review?.watchedDate ?: tanggalHariIni(),
                isRewatchAwal = review?.isRewatch ?: false,
                onSimpan = { rating, tanggal, isRewatch, catatan ->
                    viewModel.onSaveReview(rating, tanggal, isRewatch, catatan)
                },
                onHapus = { viewModel.onDeleteReview() },
                onTutup = { viewModel.onCloseSheet() }
            )
        }
    }
}

// Tombol Simpan: stateless, ikon bookmark terisi bila tersimpan (state hoisting)
@Composable
private fun TombolSimpan(
    isSaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Simpan")
    }
}

// Tombol Sudah Ditonton: stateless, ikon centang terisi bila sudah (state hoisting)
@Composable
private fun TombolSudahDitonton(
    isWatched: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            if (isWatched) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Sudah Ditonton")
    }
}

/**
 * Format durasi dari menit ke "Xj Ym".
 */
private fun formatDurasi(menit: Int?): String {
    if (menit == null || menit <= 0) return ""
    val jam = menit / 60
    val sisaMenit = menit % 60
    return listOfNotNull(
        "${jam}j".takeIf { jam > 0 },
        "${sisaMenit}m".takeIf { sisaMenit > 0 }
    ).joinToString(" ")
}

/**
 * Buka trailer di aplikasi YouTube. Dipakai tombol thumbnail
 * dan tombol cadangan "Tonton di YouTube".
 */
private fun bukaTrailer(context: android.content.Context, idTrailer: String) {
    val url = android.net.Uri.parse("https://www.youtube.com/watch?v=$idTrailer")
    context.startActivity(Intent(Intent.ACTION_VIEW, url))
}

/**
 * Ambil inisial dari nama: huruf pertama dari maksimal dua kata pertama, uppercase.
 */
private fun inisial(nama: String): String {
    return nama.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }
}

/**
 * Teks tanggal hari ini format Indonesia, mis. "12 Mei 2024".
 */
private fun tanggalHariIni(): String {
    val format = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
    return format.format(Date())
}

@Composable
private fun SectionUlasanSaya(
    review: ReviewEntity?,
    onEdit: () -> Unit
) {
    Column {
        Text(
            text = "Ulasan Saya",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (review != null) {
                    BarisBintang(rating = review.rating)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (review.note.isNotEmpty()) {
                        Text(
                            text = review.note,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = if (review.isRewatch) {
                            "Ditonton: ${review.watchedDate} • Tonton ulang"
                        } else {
                            "Ditonton: ${review.watchedDate}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onEdit) {
                        Text("Edit")
                    }
                } else {
                    Text(
                        text = "Belum ada ulasan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTrailer(
    film: MovieDetail,
    context: android.content.Context
) {
    Column {
        Text(
            text = "Trailer",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (film.trailerId != null) {
            val idTrailer = film.trailerId
            val thumbnailUrl = "https://img.youtube.com/vi/$idTrailer/hqdefault.jpg"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clickable { bukaTrailer(context, idTrailer) },
                shape = MaterialTheme.shapes.medium
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Thumbnail trailer",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Overlay play icon di tengah
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Putar trailer",
                            modifier = Modifier.size(64.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { bukaTrailer(context, idTrailer) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tonton di YouTube")
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Text(
                    text = "Trailer tidak tersedia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionSinopsis(film: MovieDetail) {
    Column {
        Text(
            text = "Sinopsis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Materi State: rememberSaveable mempertahankan nilai saat layar diputar
        var terbuka by rememberSaveable { mutableStateOf(false) }

        Text(
            text = film.overview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = if (terbuka) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (terbuka) "Tutup" else "Baca selengkapnya",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { terbuka = !terbuka }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sutradara & Penulis
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sutradara",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = film.director.ifEmpty { "-" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Penulis",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (film.writers.isNotEmpty()) film.writers.joinToString(", ") else "-",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SectionPemeran(cast: List<CastMember>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pemeran Utama",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${cast.size} Aktor",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Parameter 'key' WAJIB di Lazy Layout (materi Lazy)
            items(cast, key = { it.name }) { member ->
                Column(
                    modifier = Modifier.width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar inisial
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = inisial(member.name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = member.character,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}