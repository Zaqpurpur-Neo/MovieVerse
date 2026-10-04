package com.verse.movieverse.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.verse.movieverse.ui.common.UiState

/**
 * Materi UiState: satu komponen memuat tiga keadaan (Loading/Error/Success)
 * supaya tiap bagian layar seragam dan bisa punya state sendiri.
 * Lebar dan tinggi monetize pemanggil lewat modifier.
 */
@Composable
fun <T> BagianMuat(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    konten: @Composable (T) -> Unit
) {
    when (state) {
        is UiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Coba Lagi")
                }
            }
        }

        is UiState.Success -> konten(state.data)
    }
}