package com.verse.movieverse.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Delegasi top-level untuk Preferences DataStore.
// Instance DataStore dibuat otomatis saat pertama kali diakses.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_prefs")

class ProfileStore(private val context: Context) {
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val PHOTO_PATH_KEY = stringPreferencesKey("photo_path")

    // Flow nama dengan nilai default "Pengguna" bila belum pernah diisi
    val namaFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[NAME_KEY] ?: "Pengguna"
    }

    // Flow path foto, null bila pengguna belum memilih foto
    val photoPathFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PHOTO_PATH_KEY]
    }

    suspend fun saveName(nama: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = nama
        }
    }

    suspend fun savePhotoPath(path: String) {
        context.dataStore.edit { prefs ->
            prefs[PHOTO_PATH_KEY] = path
        }
    }

    // Menghapus semua preferensi (dipanggil saat Reset Data Lokal)
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}