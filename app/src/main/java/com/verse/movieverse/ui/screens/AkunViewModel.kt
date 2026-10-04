package com.verse.movieverse.ui.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.local.AppDatabase
import com.verse.movieverse.data.local.ProfileStore
import com.verse.movieverse.data.local.ReviewEntity
import com.verse.movieverse.data.local.WatchedEntity
import com.verse.movieverse.data.local.WatchlistEntity
import com.verse.movieverse.data.repository.PersonalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

class AkunViewModel(
    private val context: Context,
    private val profileStore: ProfileStore,
    private val database: AppDatabase,
    private val personalRepository: PersonalRepository
) : ViewModel() {

    val nama: StateFlow<String> = profileStore.namaFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, "Pengguna")

    val photoPath: StateFlow<String?> = profileStore.photoPathFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // StateFlow daftar dari Room (Fase 7C)
    val reviews: StateFlow<List<ReviewEntity>> = personalRepository.semuaReviews()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val watchlist: StateFlow<List<WatchlistEntity>> = personalRepository.semuaWatchlist()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val watched: StateFlow<List<WatchedEntity>> = personalRepository.semuaWatched()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSaveName(namaBaru: String) {
        val trimmed = namaBaru.trim()
        if (trimmed.isEmpty() || trimmed.length > 30) return
        viewModelScope.launch {
            profileStore.saveName(trimmed)
        }
    }

    fun onPhotoPicked(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    // Nama berkas dibuat unik (timestamp) agar path absolutnya berubah.
                    // Jika path sama, Coil menganggapnya gambar yang sama (cache) dan UI tidak diperbarui.
                    val fileName = "profile_${System.currentTimeMillis()}.jpg"
                    val newFile = File(context.filesDir, fileName)

                    FileOutputStream(newFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    inputStream.close()

                    val newPath = newFile.absolutePath
                    val oldPath = photoPath.value

                    profileStore.savePhotoPath(newPath)

                    if (oldPath != null && oldPath != newPath) {
                        val oldFile = File(oldPath)
                        if (oldFile.exists()) {
                            oldFile.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                // Abaikan error untuk kesederhanaan UTS
            }
        }
    }

    fun onResetAll() {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllTables()
            profileStore.clear()

            val currentPath = photoPath.value
            if (currentPath != null) {
                val file = File(currentPath)
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }

    // === Statistik (Fase 7C) ===
    fun jumlahFilmDinilai(): Int = reviews.value.size

    fun jumlahUlasanDitulis(): Int = reviews.value.count { it.note.isNotBlank() }

    fun jumlahWatchlist(): Int = watchlist.value.size

    fun jumlahSudahDitonton(): Int = watched.value.size

    fun rataRataRating(): Float {
        val daftar = reviews.value
        if (daftar.isEmpty()) return 0f
        return (daftar.sumOf { it.rating.toDouble() } / daftar.size).toFloat()
    }

    // Distribusi rating: 1 sampai 5. Rating dibulatkan ke bilangan bulat terdekat.
    fun distribusiRating(): Map<Int, Int> {
        val hasil = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        reviews.value.forEach { review ->
            val bintang = review.rating.roundToInt().coerceIn(1, 5)
            hasil[bintang] = (hasil[bintang] ?: 0) + 1
        }
        return hasil
    }

    // Film favorit: 4 ulasan rating tertinggi. Seri: yang terbaru (createdAt) lebih dulu.
    fun filmFavorit(): List<ReviewEntity> {
        return reviews.value
            .sortedWith(compareByDescending<ReviewEntity> { it.rating }.thenByDescending { it.createdAt })
            .take(4)
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appContext = context.applicationContext
            val db = AppDatabase.getInstance(appContext)
            val store = ProfileStore(appContext)
            val personalRepo = PersonalRepository(db)
            @Suppress("UNCHECKED_CAST")
            return AkunViewModel(appContext, store, db, personalRepo) as T
        }
    }
}