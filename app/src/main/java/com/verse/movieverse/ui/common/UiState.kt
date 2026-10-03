package com.verse.movieverse.ui.common

/**
 * Representasi status antarmuka pengguna (UI State) untuk pola Unidirectional Data Flow (UDF).
 *
 * Menggunakan sealed interface dengan tiga kemungkinan state:
 * - [Loading]: Menandakan proses pemuatan data sedang berjalan.
 * - [Success]: Menandakan pemuatan data berhasil dan membawa payload data [T].
 * - [Error]: Menandakan kegagalan pemuatan data dengan pesan error.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
