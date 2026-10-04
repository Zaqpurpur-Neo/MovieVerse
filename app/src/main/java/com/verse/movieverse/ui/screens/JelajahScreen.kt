package com.verse.movieverse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.ui.common.formatSkor
import com.verse.movieverse.ui.components.BagianMuat
import com.verse.movieverse.ui.components.PosterCard
import com.verse.movieverse.ui.components.PosterImage

/**
 * Layar Jelajah. TANPA Scaffold/TopAppBar dan TANPA spinner penuh layar:
 * kerangka layar selalu tampil, tiap bagian mengisi state-nya sendiri.
 */
@Composable
fun JelajahScreen(
    onOpenSearch: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: JelajahViewModel = viewModel()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val hero by viewModel.hero.collectAsStateWithLifecycle()
    val populer by viewModel.populer.collectAsStateWithLifecycle()
    val sedangTayang by viewModel.sedangTayang.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // a. Kolom pencarian tiruan (tidak bisa diketik).
        KotakPencarianTiruan(
            // State hoisting: aksi navigasi diteruskan ke parent lewat lambda.
            onClick = onOpenSearch
        )

        // b. Chip genre.
        ChipGenre(terpilih = selectedGenre) { genre -> viewModel.selectGenre(genre) }

        // c. Hero "Film Unggulan".
        BagianMuat(state = hero, modifier = Modifier.fillMaxWidth().height(220.dp).padding(horizontal = 16.dp), onRetry = { viewModel.retryHero() }) { film ->
            HeroCard(
                film = film,
                onOpenDetail = onOpenDetail,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // d. Section Film Populer.
        SectionHeader(
            judul = "Film Populer",
            subjudul = "Paling populer di TMDB",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        BagianMuat(state = populer, modifier = Modifier.fillMaxWidth().height(240.dp).padding(horizontal = 16.dp), onRetry = { viewModel.retryPopuler() }) { daftar ->
            if (daftar.isEmpty()) {
                Text(
                    text = "Tidak ada film untuk genre ini",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            } else {
                BarisPoster(daftar = daftar, onOpenDetail = onOpenDetail)
            }
        }

        // e. Section Sedang Tayang.
        SectionHeader(
            judul = "Sedang Tayang",
            subjudul = "Film yang sedang tayang di bioskop",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        BagianMuat(state = sedangTayang, modifier = Modifier.fillMaxWidth().height(240.dp).padding(horizontal = 16.dp), onRetry = { viewModel.retrySedangTayang() }) { daftar ->
            BarisPoster(daftar = daftar, onOpenDetail = onOpenDetail)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun KotakPencarianTiruan(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            // State hoisting: klik membuka layar pencarian lewat lambda parent.
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Cari",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Cari film, sutradara, aktor...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ChipGenre(terpilih: String, onPilih: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // Materi Lazy: key wajib agar chip dikenali dari nilainya, bukan posisi.
        items(listOf("Semua") + daftarGenre.take(5), key = { it }) { genre ->
            FilterChip(
                selected = terpilih == genre,
                onClick = { onPilih(genre) },
                label = { Text(genre) }
            )
        }
    }
}

@Composable
private fun SectionHeader(judul: String, subjudul: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = judul,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subjudul,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BarisPoster(daftar: List<MovieSummary>, onOpenDetail: (Int) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Materi Lazy: key wajib agar item poster dikenali dari id film.
        items(daftar, key = { it.id }) { film ->
            PosterCard(
                movie = film,
                // State hoisting: aksi buka detail dilempar ke parent.
                onClick = { onOpenDetail(film.id) },
                modifier = Modifier.width(130.dp)
            )
        }
    }
}

@Composable
private fun HeroCard(
    film: MovieSummary,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PosterImage(
                url = film.posterUrl,
                modifier = Modifier.fillMaxSize()
            )
            // Gradien gelap vertikal di bawah agar teks terbaca.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = "Film Unggulan",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = film.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // Badge rating.
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.width(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatSkor(film.rating),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = "${film.year} • ${film.genres.take(2).joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        // State hoisting: tombol trailer membuka detail film.
                        onClick = { onOpenDetail(film.id) },
                        shape = RoundedCornerShape(999.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.width(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tonton Trailer")
                    }
                    OutlinedButton(
                        onClick = { /* diaktifkan di Fase 6 */ },
                        shape = RoundedCornerShape(999.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.width(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simpan")
                    }
                }
            }
        }
    }
}
