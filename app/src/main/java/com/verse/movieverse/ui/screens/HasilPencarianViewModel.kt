package com.verse.movieverse.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verse.movieverse.data.model.GenreMap
import com.verse.movieverse.data.model.MovieSummary
import com.verse.movieverse.data.repository.MoviePage
import com.verse.movieverse.data.repository.MovieRepository
import com.verse.movieverse.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data hasil pencarian dari TMDB.
 * totalHasil dipakai untuk teks "Menampilkan N hasil".
 * bisaMuatLagi dipakai untuk menampilkan tombol "Muat Lebih Banyak".
 * modeGenre true bila query adalah nama genre yang dikenal GenreMap.
 */
data class HasilData(
    val query: String,
    val hasil: List<MovieSummary>,
    val totalHasil: Int,
    val bisaMuatLagi: Boolean,
    val modeGenre: Boolean
)

/**
 * ViewModel layar Hasil Pencarian.
 * Pencarian dikirim ke server TMDB, hasil dimuat per halaman.
 * State halaman dan daftar disimpan di ViewModel agar tidak hilang saat layar diputar.
 */
class HasilPencarianViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState<HasilData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HasilData>> = _uiState.asStateFlow()

    // State terpisah agar tombol "Muat Lebih Banyak" bisa menampilkan spinner
    // tanpa mengubah isi daftar hasil yang sudah tampil.
    private val _sedangMuatLagi = MutableStateFlow(false)
    val sedangMuatLagi: StateFlow<Boolean> = _sedangMuatLagi.asStateFlow()

    private var halaman = 1
    private var totalHalaman = 1
    private var queryTerakhir: String? = null

    fun cari(query: String) {
        // Jika query sama dan data sudah Success, jangan memuat ulang
        // (misal saat layar diputar dan LaunchedEffect jalan lagi).
        if (query == queryTerakhir && _uiState.value is UiState.Success) return

        queryTerakhir = query
        halaman = 1
        totalHalaman = 1
        _uiState.update { UiState.Loading }

        viewModelScope.launch {
            try {
                val page = ambil(query, 1)
                totalHalaman = page.totalPages
                val modeGenre = GenreMap.idDariNama(query) != null

                _uiState.update {
                    UiState.Success(
                        HasilData(
                            query = query,
                            hasil = page.movies,
                            totalHasil = page.totalResults,
                            bisaMuatLagi = halaman < totalHalaman,
                            modeGenre = modeGenre
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    UiState.Error("Gagal memuat data. Periksa koneksi internet Anda.")
                }
            }
        }
    }

    fun muatLagi() {
        val query = queryTerakhir ?: return
        val dataSaatIni = (_uiState.value as? UiState.Success)?.data ?: return

        // Abaikan bila sedang memuat atau memang tidak ada halaman berikutnya.
        if (_sedangMuatLagi.value || !dataSaatIni.bisaMuatLagi) return

        _sedangMuatLagi.value = true

        viewModelScope.launch {
            try {
                val halamanBaru = halaman + 1
                val page = ambil(query, halamanBaru)
                totalHalaman = page.totalPages

                // Gabungkan hasil lama dan baru, lalu buang duplikat berdasarkan id film.
                val gabungan = (dataSaatIni.hasil + page.movies).distinctBy { it.id }

                _uiState.update {
                    UiState.Success(
                        dataSaatIni.copy(
                            hasil = gabungan,
                            totalHasil = page.totalResults,
                            bisaMuatLagi = halamanBaru < totalHalaman
                        )
                    )
                }

                halaman = halamanBaru
            } catch (e: Exception) {
                // Bila gagal, daftar lama tetap tampil.
                // Tombol "Muat Lebih Banyak" tetap ada agar bisa dicoba lagi.
            } finally {
                _sedangMuatLagi.value = false
            }
        }
    }

    fun retry() {
        val query = queryTerakhir ?: return
        // Reset guard agar cari() mau memuat ulang query yang sama.
        queryTerakhir = null
        cari(query)
    }

    /**
     * Bila query adalah nama genre yang dikenal, pakai endpoint discover by genre.
     * Bila bukan genre, pakai endpoint search movie.
     */
    private suspend fun ambil(query: String, page: Int): MoviePage {
        return if (GenreMap.idDariNama(query) != null) {
            repository.getByGenre(query, page)
        } else {
            repository.search(query, page)
        }
    }
}