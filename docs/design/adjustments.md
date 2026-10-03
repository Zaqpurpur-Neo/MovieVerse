# Penyesuaian Desain MovieVerse (adjustments.md)

Dokumen ini NORMATIF. Jika bertentangan dengan screens-spec.md atau
screenshot/HTML di docs/design/, dokumen ini yang berlaku.
Alasan: desain Stitch memuat elemen yang tidak punya sumber data
(dataset hanya sampai Juli 2017, tanpa TMDB API, tanpa akun).

## Keputusan

1. Film contoh di desain (Dune, Oppenheimer, Poor Things, dst.) hanya
   contoh visual. Data asli berasal dari dataset (maks. 2017).
   Section "Rilis Terbaru" di Jelajah diganti menjadi "Baru di Katalog"
   (film rilis 2016-2017, urut tanggal rilis terbaru).
2. Backdrop (hero Jelajah dan Detail Film) tidak ada di dataset.
   Gunakan poster yang diperbesar dengan gradien gelap. Tidak ada
   field backdropUrl.
3. Hapus: badge klasifikasi usia (PG-13), foto aktor, jumlah vote.
   Pemeran ditampilkan sebagai avatar inisial nama (contoh "MC").
4. Hapus: tren persentase (+45%, +32%), chip "Pemenang Oscar" dan
   "Rilis 2024", teks "4 rilis bulan ini", segmen "Pemenang Penghargaan"
   dan "Koleksi Sutradara" di Kategori, badge "Prioritas" di Watchlist.
   Chip filter cepat pencarian menjadi: "Rating 8.0+", "Tersedia
   Trailer", "Rilis 2017". Segmen Kategori tersisa: "Genre Utama" dan
   "Era & Dekade". "Film Sedang Tren" diganti "Film Populer"
   (urut popularitas dari data), tanpa persentase.
5. Hapus/ganti teks menyesatkan: "Tersedia Offline" (dihapus),
   "terenkripsi offline" (ganti "tersimpan di perangkat"), badge
   "Offline" di header Jurnal (dihapus), "Offline Edition" dan versi
   v2.4.0 (ganti "MovieVerse v1.0.0"), badge ID ulasan #MV-2024-88
   (dihapus), "sesi menonton ke-4" (ganti teks statis "Tonton ulang:
   ya/tidak"), footer hasil pencarian "MovieVerse - Data Lokal"
   (ganti "MovieVerse").
6. Layar Akun: baris "Bahasa" dihapus (aplikasi hanya berbahasa
   Indonesia). Baris "Tema" tampil statis tanpa aksi (hanya dark).
   "Edit Profil" hanya mengubah NAMA (disimpan di DataStore).
   Avatar berupa lingkaran inisial dari nama. Badge verifikasi dan
   status sinkronisasi dihapus.
7. Fitur kecil:
   - Tonton Ulang (isRewatch) DIPERTAHANKAN, disimpan di Room.
   - Sentimen Tayangan (chip) DIBUANG dari Tulis Ulasan; tidak ada
     sentimentTags di mana pun.
   - Film Favorit di Akun dihitung otomatis: 4 ulasan dengan rating
     tertinggi (tie-break: tanggal tonton terbaru). Tanpa tabel baru.
   - Tombol "Kelola Koleksi Saya" di Kategori langsung membuka tab Jurnal.
8. Statistik: "Film Dinilai" = jumlah ulasan (semua ulasan punya
   rating). "Ulasan" = jumlah ulasan yang teksnya tidak kosong.
   Rating selalu kelipatan 0.5 (0.5 sampai 5.0); tidak ada nilai
   seperti 4.9. Predikat kualitatif (mis. "Luar Biasa") dihitung
   dari rating, tidak disimpan.
9. Atribusi, tampil di footer layar Akun: "Data film: The Movies
   Dataset (bersumber TMDb) - Trailer: MovieLens (CC BY 4.0)".
   Label rating "/ 10 TMDb" diganti "/ 10".

## Dampak ke field data (hapus dari rancangan data)
- Jelajah: featuredMovie.backdropUrl (pakai posterUrl).
- Detail Film: backdropUrl, certification, tmdbVoteCount,
  castMembers.profileImageUrl, trailerThumbnailUrl (thumbnail
  diturunkan dari ID YouTube, tidak disimpan).
- Pencarian Aktif: trendPercent; quickFilterTags sesuai butir 4.
- Jurnal: watchlistItems.isPriority; reviewId tidak ditampilkan.
- Akun: userProfile.avatarUrl, isVerified, syncStatus;
  appSettings.appLanguage.
- Form ulasan: sentimentTags.

## Catatan terbuka (diputuskan di langkah skema JSON)
- Nama genre dataset berbahasa Inggris; pemetaan ke label Indonesia.
- Sinopsis dataset berbahasa Inggris; tampil apa adanya.
- "Film Serupa": cara menentukan (genre/sutradara yang sama).
- Sumber riwayat pencarian: DataStore.

## Revisi 1 (mengalahkan butir yang bertentangan di atas)

R1. Film Serupa DIHAPUS total: section "Film Serupa" di Detail Film,
    field similarMovies, dan aksi ketuk kartu serupa. Catatan terbuka
    "Film Serupa" tidak berlaku lagi.
R2. Edit Profil mengubah NAMA dan FOTO PROFIL (mengganti butir 6).
    Foto dipilih lewat Photo Picker bawaan Android
    (ActivityResultContracts.PickVisualMedia), disalin ke penyimpanan
    internal aplikasi, path file disimpan di DataStore bersama nama.
    Jika belum ada foto: lingkaran inisial dari nama. Foto orang pada
    desain hanyalah contoh visual.
R3. Riwayat pencarian disimpan di Room, bukan DataStore. Tabel
    SearchHistory: id (Long, autoGenerate) dan keyword (String).
    Maksimal 5 terbaru, tanpa timestamp, kata kunci yang sama tidak
    disimpan dua kali (hapus yang lama, simpan yang baru di atas).
    Aksi: ketuk item mengisi dan mencari, ikon x menghapus satu item
    (by id), "Hapus Semua" mengosongkan tabel. Bagian riwayat dibangun
    di Fase 5 (setelah Room ada); sebelum itu section riwayat tidak
    ditampilkan. Catatan terbuka "Sumber riwayat pencarian: DataStore"
    tidak berlaku lagi.
