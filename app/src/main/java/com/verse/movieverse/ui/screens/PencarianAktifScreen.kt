package com.verse.movieverse.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.verse.movieverse.data.local.SearchHistoryEntity
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.ui.common.UiState
import com.verse.movieverse.ui.components.PosterImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PencarianAktifScreen(
    onSearch: (String) -> Unit,
    onOpenDetail: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Inisialisasi ViewModel lewat Factory sederhana
    val viewModel: PencarianAktifViewModel = viewModel(factory = PencarianAktifViewModel.Factory(context))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle(initialValue = emptyList())

    // Materi State: rememberSaveable mempertahankan nilai saat layar diputar / konfigurasi berubah.
    var query by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Fokus otomatis ke kolom teks saat layar pertama kali dibuka.
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Fungsi lokal untuk memicu pencarian dan menyimpan ke riwayat Room
    fun lakukanPencarian(keyword: String) {
        if (keyword.isNotBlank()) {
            val trimmed = keyword.trim()
            viewModel.saveSearch(trimmed)
            onSearch(trimmed)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BarisPencarianAtas(
            query = query,
            onQueryChange = { query = it },
            onClear = { query = "" },
            onSearch = { lakukanPencarian(query) },
            onNavigateUp = onNavigateUp,
            focusRequester = focusRequester
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section Pencarian Terakhir
        if (searchHistory.isNotEmpty()) {
            SectionRiwayatPencarian(
                history = searchHistory,
                onItemClick = { keyword ->
                    query = keyword
                    lakukanPencarian(keyword)
                },
                onDeleteItem = { keyword -> viewModel.deleteSearch(keyword) },
                onClearAll = { viewModel.clearSearchHistory() }
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Text(
                text = "Belum ada pencarian",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        SectionGenrePopuler(onSearch = { genre -> lakukanPencarian(genre) })

        Spacer(modifier = Modifier.height(24.dp))

        SectionFilmPopuler(
            uiState = uiState,
            onOpenDetail = onOpenDetail,
            onRetry = { viewModel.retry() }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SectionRiwayatPencarian(
    history: List<SearchHistoryEntity>,
    onItemClick: (String) -> Unit,
    onDeleteItem: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pencarian Terakhir",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = onClearAll) {
                Text("Hapus Semua")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        history.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.keyword) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.keyword,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDeleteItem(item.keyword) }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BarisPencarianAtas(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    onNavigateUp: () -> Unit,
    focusRequester: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // State hoisting: aksi navigasi kembali diteruskan ke parent lewat lambda.
        IconButton(onClick = onNavigateUp) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = "Cari film, sutradara, aktor...",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            shape = MaterialTheme.shapes.large,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SectionGenrePopuler(onSearch: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Genre Populer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val daftarGenre = listOf("Aksi", "Sci-Fi", "Drama", "Horor", "Animasi", "Thriller", "Komedi")
            daftarGenre.forEach { genre ->
                AssistChip(
                    onClick = { onSearch(genre) },
                    label = { Text(genre) }
                )
            }
        }
    }
}

@Composable
private fun SectionFilmPopuler(
    uiState: UiState<List<MovieSummary>>,
    onOpenDetail: (Int) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Film Populer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Paling populer di katalog",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Coba Lagi")
                    }
                }
            }

            is UiState.Success -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.data.forEachIndexed { index, film ->
                        BarisFilmPopuler(
                            nomor = index + 1,
                            film = film,
                            // State hoisting: aksi buka detail diteruskan ke parent lewat lambda.
                            onClick = { onOpenDetail(film.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BarisFilmPopuler(
    nomor: Int,
    film: MovieSummary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nomor.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(32.dp)
        )

        PosterImage(
            url = film.posterUrl,
            modifier = Modifier
                .width(48.dp)
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", film.rating),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${film.year} • ${film.genres.take(2).joinToString(", ")}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}   