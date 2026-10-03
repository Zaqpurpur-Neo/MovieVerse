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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.ui.common.UiState
import com.verse.movieverse.ui.components.PosterCard
import com.verse.movieverse.ui.components.PosterImage

@Composable
fun JelajahScreen(
    onOpenSearch: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: JelajahViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.retry() }) {
                    Text("Coba Lagi")
                }
            }
        }

        is UiState.Success -> {
            val movies = state.data

            // Materi State: remember digunakan untuk menghitung daftar turunan
            // hanya saat movies atau selectedGenre berubah, menghindari komputasi ulang tiap recomposition.
            val featuredMovie = remember(movies) {
                movies.maxByOrNull { it.popularity }
            }

            val popularMovies = remember(movies, selectedGenre) {
                movies.filter { selectedGenre == "Semua" || it.genres.contains(selectedGenre) }
                    .sortedByDescending { it.popularity }
                    .take(10)
            }

            val recentMovies = remember(movies, selectedGenre) {
                movies.filter { it.year >= 2016 && (selectedGenre == "Semua" || it.genres.contains(selectedGenre)) }
                    .sortedByDescending { it.releaseDate }
                    .take(10)
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // a. Kolom pencarian tiruan
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // State hoisting: aksi navigasi diteruskan ke parent lewat lambda
                        .clickable { onOpenSearch() },
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

                // b. Chip genre
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    // Parameter 'key' WAJIB di Lazy Layout (materi Lazy)
                    items(listOf("Semua", "Aksi", "Drama", "Sci-Fi", "Horor"), key = { it }) { genre ->
                        FilterChip(
                            selected = selectedGenre == genre,
                            onClick = { viewModel.selectGenre(genre) },
                            label = { Text(genre) }
                        )
                    }
                }

                // c. Hero "Film Unggulan"
                featuredMovie?.let { movie ->
                    HeroCard(
                        movie = movie,
                        onTrailerClick = { onOpenDetail(movie.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                // d. Section "Film Populer"
                SectionHeader(
                    title = "Film Populer",
                    subtitle = "Paling populer di katalog",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                if (popularMovies.isEmpty()) {
                    Text(
                        text = "Tidak ada film untuk genre ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                } else {
                    MovieRow(movies = popularMovies, onMovieClick = onOpenDetail)
                }

                // e. Section "Baru di Katalog"
                SectionHeader(
                    title = "Baru di Katalog",
                    subtitle = "Film rilis 2016-2017",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                if (recentMovies.isEmpty()) {
                    Text(
                        text = "Tidak ada film untuk genre ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                } else {
                    MovieRow(movies = recentMovies, onMovieClick = onOpenDetail)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HeroCard(
    movie: MovieSummary,
    onTrailerClick: () -> Unit,
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
                url = movie.posterUrl,
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
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${movie.year} • ${movie.genres.take(2).joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onTrailerClick,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tonton Trailer")
                    }
                    OutlinedButton(
                        onClick = { /* diaktifkan di Fase 6 */ },
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MovieRow(
    movies: List<MovieSummary>,
    onMovieClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Parameter 'key' WAJIB di Lazy Layout (materi Lazy)
        items(movies, key = { it.id }) { movie ->
            PosterCard(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.width(130.dp)
            )
        }
    }
}