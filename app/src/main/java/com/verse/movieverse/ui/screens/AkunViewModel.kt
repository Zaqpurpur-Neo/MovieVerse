package com.verse.movieverse.ui.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

    companion object {
        // Factory sederhana untuk menyuntikkan Context / Repository tanpa Hilt/Koin.
        fun factory(context: Context) = viewModelFactory {
            initializer {
                val appContext = context.applicationContext
                val db = AppDatabase.getInstance(appContext)
                val store = ProfileStore(appContext)
                AkunViewModel(appContext, store, db, PersonalRepository(db))
            }
        }
    }
}