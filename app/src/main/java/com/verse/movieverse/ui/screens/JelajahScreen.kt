package com.verse.movieverse.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.verse.movieverse.ui.common.UiState

/**
 * State hoisting: Aksi navigasi (onOpenSearch, onOpenDetail) diteruskan sebagai lambda
 * dari parent (AppNavigation), sehingga Screen ini tidak perlu tahu tentang NavController.
 */
@Composable
fun JelajahScreen(
    onOpenSearch: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: JelajahViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Berhasil memuat ${movies.size} film",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onOpenSearch) {
                        Text("Buka Pencarian")
                    }
                    Button(onClick = { onOpenDetail(157336) }) {
                        Text("Buka Detail (id 157336)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    // Parameter 'key' WAJIB di Lazy Layout agar recomposition efisien
                    // dan state visual (misal: animasi atau scroll) item terjaga saat data berubah.
                    items(movies.take(10), key = { it.id }) { movie ->
                        Text(
                            text = "${movie.title} (${movie.year})",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}