# Spesifikasi Layar MovieVerse (Screens Specification)

Dokumen ini mendefinisikan arsitektur antarmuka dan spesifikasi fungsional untuk 9 layar pada MovieVerse berdasarkan visual screenshot dan berkas HTML desain Stitch.

---

## 1. Layar Jelajah (`jelajah`)

Layar beranda utama untuk mengeksplorasi katalog film, film unggulan, rilis populer, dan rilisan terbaru.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Logo aplikasi MovieVerse & teks brand.
   - Tombol ikon pencarian (`search`).
2. **Search Box Anchor & Quick Filter Chips**:
   - Kotak pencarian statis (`Cari film, sutradara, aktor...`) dengan ikon pencarian dan tombol filter (`tune`).
   - Carousel filter chip horizontal: `Semua` (aktif), `Aksi`, `Drama`, `Sci-Fi`, `Horor`, `Komedi`, `Animasi`.
3. **Hero Feature Card (Film Unggulan)**:
   - Backdrop visual besar dengan overlay gradien gelap.
   - Badge pill di kiri atas: `Film Unggulan` (dengan ikon verified).
   - Info film: Judul film (`Dune: Part Two`), metadata (`Tahun • Genre • Sub-genre`), badge rating bintang emas (`8.6`).
   - Aksi cepat: Tombol `Tonton Trailer` (ikon play) dan tombol `Simpan` (bookmark).
4. **Section Film Populer**:
   - Header section: Judul `Film Populer`, deskripsi singkat (`Paling banyak ditonton minggu ini`), tombol link `Lihat Semua >`.
   - Horizontal poster stream: Kartu poster film rasio 2:3 dengan badge rating di sudut kanan atas, judul film, dan metadata (contoh: *Poor Things*, *Oppenheimer*, *Past Lives*, *The Boy & The Heron*).
5. **Section Rilis Terbaru**:
   - Header section: Judul `Rilis Terbaru`, deskripsi singkat.
   - Grid 2-kolom kartu poster vertikal dengan badge rating, judul film, dan tahun/genre (contoh: *Anatomy of a Fall*, *The Zone of Interest*).
6. **Bottom Navigation Bar**:
   - 4 tab navigasi: `Jelajah` (aktif dengan pill indicator), `Kategori`, `Jurnal`, `Akun`.

### Data yang Dibutuhkan (Field Names)
- `featuredMovie`: `id`, `title`, `releaseYear`, `genres`, `rating`, `backdropUrl`, `trailerUrl`, `isBookmarked`
- `popularMovies`: List of (`id`, `title`, `releaseYear`, `genres`, `rating`, `posterUrl`)
- `recentReleases`: List of (`id`, `title`, `releaseYear`, `genres`, `rating`, `posterUrl`)
- `genreFilters`: List of `genreName`
- `selectedGenre`: `genreName`

### Aksi Pengguna
- Mengetuk Search Box / Ikon Search $\rightarrow$ Membuka layar pencarian aktif (`pencarian-aktif`).
- Mengetuk chip filter genre $\rightarrow$ Memfilter katalog sesuai genre terpilih.
- Mengetuk tombol `Tonton Trailer` pada Hero Card $\rightarrow$ Memutar video trailer / membuka pemutar trailer.
- Mengetuk tombol `Simpan` pada Hero Card $\rightarrow$ Menambah/menghapus film dari Watchlist.
- Mengetuk kartu film $\rightarrow$ Navigasi ke layar `detail-film`.
- Mengetuk `Lihat Semua` $\rightarrow$ Navigasi ke daftar film populer lengkap / filter kategori.
- Mengetuk item Bottom Navigation $\rightarrow$ Berpindah tab layar.

### State UI
- **Loading**: Menampilkan shimmer/skeleton placeholder untuk Hero banner, baris filter chip, carousel poster horizontal, dan grid 2-kolom.
- **Success**: Menampilkan konten data film lengkap sesuai tata letak.
- **Empty**: Tampilan pesan "Tidak ada data film tersedia" dengan tombol muat ulang jika katalog lokal kosong.
- **Error**: Menampilkan banner/kartu error dengan pesan kegagalan dan tombol retry.

---

## 2. Layar Detail Film (`detail-film`)

Layar komprehensif untuk menampilkan informasi detail satu film, trailer, sinopsis, daftar pemeran, ulasan pribadi tersimpan, dan rekomendasi film serupa.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Tombol kembali (`arrow_back`).
   - Judul halaman (`Detail Film`).
   - Tombol bagikan (`share`).
2. **Hero Backdrop Media**:
   - Gambar latar sinematik layar penuh (rasio 16:10) dengan gradien gelap bawah dan samping.
3. **Header Metadata Film**:
   - Judul utama film (`Interstellar`).
   - Badge rating global TMDb (ikon bintang emas, skor `8.7`, `/ 10 TMDb`).
   - Baris info: `Tahun (2014)` • `Durasi (2j 49m)` • Badge klasifikasi usia (`PG-13`).
   - Chip genre horizontal (`Sci-Fi`, `Petualangan`, `Drama`).
4. **Action Buttons Grid**:
   - Tombol toggle `Simpan` / Bookmark.
   - Tombol toggle `Sudah Ditonton` (ikon centang).
   - Tombol utama `Tulis Ulasan Saya` (atau `Edit Ulasan` jika sudah ada ulasan).
5. **Section Trailer**:
   - Header section dengan ikon film dan judul `Trailer` (`Trailer Resmi #1`).
   - Thumbnail pemutar video 16:9 dengan overlay tombol Play besar di tengah.
   - Link eksternal `Buka di YouTube` dengan ikon open.
6. **Section Sinopsis & Sutradara**:
   - Teks sinopsis panjang (dapat diperluas dengan tombol `Baca selengkapnya`).
   - Grid 2-kolom info: `Sutradara` (e.g. Christopher Nolan) dan `Penulis` (e.g. Jonathan Nolan & Christopher Nolan).
7. **Section Pemeran Utama**:
   - Header `Pemeran Utama` dan jumlah aktor (`5 Aktor`).
   - Carousel horizontal avatar pemeran (lingkaran foto dengan border) dengan nama aktor dan nama karakter (e.g. Matthew McConaughey sebagai Cooper, Anne Hathaway sebagai Brand, Jessica Chastain sebagai Murph, Michael Caine, Matt Damon).
8. **Section Ulasan Saya (Personal Review Card)**:
   - Header `Ulasan Saya` dengan indikator status `Tersimpan secara lokal di perangkat` dan tombol teks `Edit Ulasan`.
   - Kartu ulasan: Skor rating bintang besar (`5.0 / 5.0`), tanggal pencatatan (`Dicatat pada 12 Mei 2024`), teks ulasan/catatan refleksi, dan catatan privasi (`Catatan ini hanya terlihat oleh Anda secara privat`).
9. **Section Film Serupa**:
   - Header `Film Serupa` dan subjudul rekomendasi.
   - Grid 3-kolom kartu film rekomendasi dengan poster, rating bintang, judul, dan tahun (e.g. *Arrival*, *The Martian*, *Gravity*).

### Data yang Dibutuhkan (Field Names)
- `movieDetail`: `id`, `title`, `backdropUrl`, `posterUrl`, `releaseYear`, `runtime`, `certification`, `genres`, `tmdbRating`, `tmdbVoteCount`, `synopsis`, `director`, `writers`, `trailerYoutubeUrl`, `trailerThumbnailUrl`, `isWatchlisted`, `isWatched`
- `castMembers`: List of (`castId`, `name`, `characterName`, `profileImageUrl`)
- `userReview`: `reviewId`, `ratingScore`, `watchDate`, `reviewText`, `isRewatch`, `createdAt` (nullable)
- `similarMovies`: List of (`id`, `title`, `releaseYear`, `rating`, `posterUrl`)

### Aksi Pengguna
- Mengetuk tombol kembali $\rightarrow$ Kembali ke layar sebelumnya.
- Mengetuk tombol bagikan $\rightarrow$ Membuka dialog share sistem.
- Mengetuk tombol `Simpan` $\rightarrow$ Toggle status Watchlist.
- Mengetuk tombol `Sudah Ditonton` $\rightarrow$ Toggle status tonton.
- Mengetuk tombol `Tulis Ulasan Saya` / `Edit Ulasan` $\rightarrow$ Membuka bottom sheet formulir ulasan.
- Mengetuk thumbnail trailer / link YouTube $\rightarrow$ Memutar trailer film.
- Mengetuk `Baca selengkapnya` $\rightarrow$ Membuka seluruh teks sinopsis.
- Mengetuk kartu film serupa $\rightarrow$ Membuka detail film yang dipilih.

### State UI
- **Loading**: Skeleton placeholder untuk backdrop, detail header, tombol aksi, trailer player, sinopsis, avatar cast, dan kartu film serupa.
- **Success**: Seluruh data film ditampilkan. Jika user belum menulis ulasan, kartu ulasan menampilkan tombol ajakan membuat ulasan.
- **Empty**: Tampilan film tidak ditemukan jika ID film tidak valid.
- **Error**: Pesan kegagalan memuat data dengan tombol coba lagi.

---

## 3. Layar Hasil Pencarian (`hasil-pencarian`)

Layar menampilkan hasil pencarian berdasarkan kata kunci pencarian, lengkap dengan filter baris, ringkasan jumlah hasil, daftar kartu film hasil pencarian, dan tombol muat lebih banyak.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Tombol kembali (`arrow_back`).
   - Judul `MovieVerse`.
   - Tombol pintasan `Bookmarks / Watchlist`.
2. **Query Input Bar & Search Info**:
   - Kotak pencarian dengan ikon search, teks query aktif (e.g. `Christopher Nolan`), tombol hapus query (`close`), dan tombol filter lanjutan (`tune`).
   - Baris meta hasil: `Menampilkan 24 hasil film untuk “Christopher Nolan”`.
3. **Horizontal Segmented Tabs**:
   - Tab tombol: `Semua` (aktif dengan ikon centang), `Genre v`, `Tahun v`, `Skor v`.
4. **Daftar Film Hasil Pencarian (Vertical Movie List)**:
   - Header daftar: Aksen bar ungu, judul `Daftar Film`, dan label pengurutan `Urutkan: Popularitas`.
   - Kartu film horizontal berlatar `surfaceContainer`:
     - Poster mini (rasio 2:3) dengan badge rating bintang di sudut bawah.
     - Judul film tebal (e.g. *Oppenheimer*, *Interstellar*, *Inception*, *Dunkirk*, *The Dark Knight*).
     - Tombol toggle bookmark cepat di kanan atas kartu.
     - Metadata (`Tahun • Genre`).
     - Cuplikan sinopsis singkat (2 baris teks).
     - Footer kartu: Badge `Trailer` dan label `Tersedia Offline`.
5. **End of Results & Pagination Button**:
   - Tombol `Muat Lebih Banyak (14 film lagi)` dengan ikon `expand_circle_down`.
   - Label footer `MovieVerse • Data Lokal`.

### Data yang Dibutuhkan (Field Names)
- `searchQuery`: `queryText`
- `resultCount`: `totalResults`
- `activeTab`: `selectedTab` (Semua / Genre / Tahun / Skor)
- `searchResults`: List of (`id`, `title`, `releaseYear`, `genres`, `rating`, `overview`, `posterUrl`, `isWatchlisted`, `hasTrailer`)
- `hasMoreResults`: `boolean`
- `remainingCount`: `number`

### Aksi Pengguna
- Mengetik / mengubah teks di input bar $\rightarrow$ Memperbarui pencarian.
- Mengetuk tombol `close` $\rightarrow$ Mengosongkan teks pencarian.
- Mengetuk tab filter (Semua, Genre, Tahun, Skor) $\rightarrow$ Memfilter hasil pencarian.
- Mengetuk tombol bookmark pada kartu $\rightarrow$ Toggle status watchlist film tersebut.
- Mengetuk kartu film $\rightarrow$ Navigasi ke `detail-film`.
- Mengetuk `Muat Lebih Banyak` $\rightarrow$ Memuat batch data berikutnya.

### State UI
- **Loading**: Indikator loading atau skeleton card list.
- **Success**: Menampilkan daftar kartu film sesuai query dan filter.
- **Empty**: Tampilan ilustrasi/pesan "Tidak ada film yang cocok dengan kata kunci '[query]'".
- **Error**: Pesan gagal mencari data dengan opsi ulangi pencarian.

---

## 4. Layar Pencarian Aktif / Overlay (`pencarian-aktif`)

Layar interaktif saat bilah pencarian sedang aktif terfokus, menampilkan riwayat pencarian terbaru, genre populer, daftar tren hari ini, dan simulasi keyboard virtual.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Tombol kembali (`arrow_back`).
   - Judul `Cari di MovieVerse`.
   - Tombol filter pencarian (`tune`).
2. **Active Search Input Bar (Sticky)**:
   - Kotak input dengan border aktif ungu, kursor berkedip (blinking cursor), teks kata kunci aktif (`Dune: Part Two`), dan tombol hapus (`close`).
3. **Quick Criteria Filter Pills**:
   - Carousel filter cepat: `Rating 8.0+ TMDB` (aktif), `Tersedia Trailer`, `Rilis 2024`, `Pemenang Oscar`.
4. **Section Pencarian Terakhir (Recent Searches)**:
   - Header `Pencarian Terakhir` (ikon riwayat) dan tombol `Hapus Semua`.
   - Daftar riwayat pencarian (e.g. *Dune: Part Two*, *Oppenheimer*, *Christopher Nolan*) dengan ikon jadwal/jam dan tombol hapus individual (`close`).
5. **Section Genre Populer**:
   - Header `Genre Populer`.
   - Kumpulan pill genre: `Aksi`, `Sci-Fi`, `Drama`, `Horor`, `Animasi`, `Thriller`, `Komedi`.
6. **Section Film Sedang Tren (Trending Today)**:
   - Header `Film Sedang Tren` dengan ikon api dan label `Hari ini`.
   - Daftar peringkat film dengan nomor urut besar (1, 2, 3, 4), thumbnail poster, judul film, rating bintang emas, tahun & genre, serta persentase kenaikan tren (e.g. `+45%`, `+32%`).
7. **Virtual Keyboard Bottom Drawer**:
   - Baris tombol alfabet (QWERTY), tombol shift, backspace, numeric switcher `?123`, tombol spasi, dan tombol aksi `Cari`.

### Data yang Dibutuhkan (Field Names)
- `currentSearchText`: `text`
- `recentSearches`: List of (`id`, `keyword`, `timestamp`)
- `quickFilterTags`: List of (`id`, `label`, `iconName`, `isSelected`)
- `popularGenres`: List of `genreName`
- `trendingMovies`: List of (`rank`, `id`, `title`, `rating`, `releaseYear`, `genreText`, `posterUrl`, `trendPercent`)

### Aksi Pengguna
- Mengetik pada keyboard / input field $\rightarrow$ Memperbarui teks pencarian.
- Mengetuk tombol `Cari` / menekan Enter $\rightarrow$ Menjalankan pencarian dan navigasi ke `hasil-pencarian`.
- Mengetuk item riwayat pencarian $\rightarrow$ Mengisi kotak pencarian dengan teks riwayat lalu mencari.
- Mengetuk ikon hapus pada satu riwayat $\rightarrow$ Menghapus item dari database riwayat lokal.
- Mengetuk `Hapus Semua` $\rightarrow$ Menghapus seluruh riwayat pencarian.
- Mengetuk chip genre populer $\rightarrow$ Melakukan pencarian berdasarkan genre yang dipilih.
- Mengetuk item film tren $\rightarrow$ Navigasi ke `detail-film`.

### State UI
- **Loading**: Placeholder shimmer saat memuat daftar film tren.
- **Success**: Menampilkan riwayat pencarian, chip genre, dan daftar tren.
- **Empty**: Jika riwayat kosong, menampilkan teks "Belum ada riwayat pencarian" dan menyembunyikan tombol "Hapus Semua".
- **Error**: Pesan error jika gagal memuat data tren lokal.

---

## 5. Layar Kategori & Kurasi (`kategori`)

Layar untuk menjelajahi seluruh film berdasarkan mosaik genre dan koleksi tematik pilihan.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Ikon logo film berlatar `primaryContainer`, judul `MovieVerse`, dan subjudul `Katalog Film Pribadi`.
   - Tombol aksi: `search` dan `filter_list`.
2. **Search Header Module**:
   - Kotak input filter kurasi (`Cari genre, tema, atau koleksi film...`) dan tombol `tune`.
3. **Filter & Segment Chips Carousel**:
   - Chip kategori: `Genre Utama` (aktif dengan ikon category), `Pemenang Penghargaan`, `Koleksi Sutradara`, `Era & Dekade`.
4. **Section Kategori Film (Genre Mosaic Grid)**:
   - Header `Kategori Film`, subjudul, dan tombol `Semua >`.
   - Grid 2-kolom kartu genre interaktif:
     - Ikon genre dalam kontainer (e.g. bela diri untuk Aksi, roket untuk Sci-Fi, topeng teater untuk Drama, tengkorak untuk Horor & Misteri, animasi untuk Animasi, psikologi untuk Thriller, senyum untuk Komedi, video kamera untuk Dokumenter).
     - Judul genre dan jumlah film (e.g. `3.420 film`, `2.810 film`, `5.190 film`).
     - Panah navigasi (`arrow_forward`).
5. **Section Koleksi Pilihan (Curated Thematic Lists)**:
   - Header dengan ikon bintang `Koleksi Pilihan` dan subjudul kurasi tematik.
   - Daftar kartu kurasi horizontal besar:
     - Banner thumbnail visual kurasi dengan badge jumlah film.
     - Tag kurasi (e.g. `Rating Terbaik`, `Era Emas`, `Trending`).
     - Judul koleksi (e.g. *Skor Tertinggi*, *Klasik Sepanjang Masa*, *Rilis Terbaru*).
     - Deskripsi tema kurasi.
     - Footer kartu: Info jumlah film dan tombol buka koleksi (`arrow_forward`).
6. **Community/Call-to-Action Card**:
   - Kartu promosi: Ikon perpustakaan video, judul `Organisir Koleksi Film Anda`, deskripsi, dan tombol aksi `Kelola Koleksi Saya`.
7. **Bottom Navigation Bar**:
   - 4 tab navigasi dengan tab `Kategori` aktif.

### Data yang Dibutuhkan (Field Names)
- `curationSearchQuery`: `text`
- `activeCategorySegment`: `segmentName`
- `genresList`: List of (`genreId`, `name`, `movieCount`, `iconName`)
- `curatedCollections`: List of (`collectionId`, `title`, `tagLabel`, `subTag`, `description`, `movieCount`, `coverImageUrl`)

### Aksi Pengguna
- Mengetik pada kotak pencarian kurasi $\rightarrow$ Menyaring genre atau koleksi yang ditampilkan.
- Memilih segmen chip kategori $\rightarrow$ Mengubah tampilan kelompok kurasi.
- Mengetuk kartu genre $\rightarrow$ Membuka daftar film dalam genre tersebut.
- Mengetuk kartu koleksi pilihan $\rightarrow$ Membuka halaman detail koleksi tematik.
- Mengetuk tombol `Kelola Koleksi Saya` $\rightarrow$ Navigasi ke tab `Jurnal` atau manajemen tag pribadi.
- Mengetuk item Bottom Navigation $\rightarrow$ Berpindah tab layar.

### State UI
- **Loading**: Skeleton placeholder untuk grid genre 2-kolom dan kartu koleksi.
- **Success**: Menampilkan seluruh kartu genre dan koleksi pilihan.
- **Empty**: Pesan "Kategori atau koleksi tidak ditemukan" jika hasil filter kosong.
- **Error**: Tampilan error kartu dengan tombol muat ulang.

---

## 6. Layar Jurnal (`jurnal`)

Layar manajemen catatan pribadi, log penayangan film, watchlist, dan riwayat film yang sudah ditonton secara offline.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Ikon film, judul `MovieVerse`, badge status `Offline` (lingkaran hijau), dan subjudul `Jurnal & Koleksi Pribadi`.
   - Tombol pencarian catatan film (`search`).
2. **Header Title Section**:
   - Judul `Jurnal`, deskripsi (`Catatan apresiasi & ulasan film pribadi • 100% tersimpan di perangkat`), dan ikon buku cerita.
3. **Bento Summary Cards (Metrik Review Pribadi)**:
   - Grid 2x2 kartu ringkasan:
     - **Film Dinilai**: Jumlah total (`68 film`) dengan bar progres.
     - **Ulasan Ditulis**: Jumlah catatan (`24 catatan`) dengan bar progres.
     - **Rata-rata Skor**: Nilai rata-rata (`4.2 / 5.0`) dengan representasi 5 bintang.
     - **Watchlist**: Jumlah antrean (`18 antrean`) dan info rilis baru.
4. **Filter Ribbon Tabs**:
   - Tab pilihan: `Ulasan Saya` (aktif), `Watchlist` (dengan badge angka 18), `Sudah Ditonton`.
5. **Konten Tab - Ulasan Saya (Stream Kartu Ulasan)**:
   - Kartu ulasan film:
     - Poster mini film dengan badge tahun rilis di kiri atas.
     - Judul film (e.g. *Oppenheimer*, *Blade Runner 2049*, *Interstellar*, *Spirited Away*), tanggal log tonton, tag `Tonton Ulang` (jika ada), dan badge `Tersimpan` offline.
     - Nilai rating bintang emas (e.g. 5.0, 4.0, 4.5).
     - Kutipan teks ulasan pribadi.
     - Baris aksi kartu: Tombol `Edit`, tombol `Bagikan`, dan tombol `Hapus` (warna error).
   - Banner akhir catatan: `Akhir Catatan Anda` dengan konfirmasi enkripsi offline.
6. **Konten Tab - Watchlist**:
   - Kartu antrean watchlist dengan poster, judul, genre, durasi, badge `Prioritas`, sinopsis ringkas, tombol `Tandai Sudah Ditonton`, dan tombol `Hapus dari watchlist`.
7. **Konten Tab - Sudah Ditonton**:
   - Kartu film yang telah ditonton dengan poster, tanggal selesai ditonton, rating, sinopsis singkat, tombol `Tulis Ulasan` / `Lihat Catatan`, dan tombol `Tonton Lagi`.
8. **Floating Action Button (FAB)**:
   - Tombol mengambang di kanan bawah: `Catat Film` / Tambah Ulasan Baru (ikon tambah).
9. **Bottom Navigation Bar**:
   - 4 tab navigasi dengan tab `Jurnal` aktif.

### Data yang Dibutuhkan (Field Names)
- `journalMetrics`: `totalRatedMovies`, `totalWrittenReviews`, `averageScore`, `watchlistCount`, `upcomingWatchlistCount`
- `activeJournalTab`: `selectedTab` (Ulasan Saya / Watchlist / Sudah Ditonton)
- `userReviewsList`: List of (`reviewId`, `movieId`, `movieTitle`, `movieYear`, `posterUrl`, `logDate`, `isRewatch`, `rating`, `reviewContent`)
- `watchlistItems`: List of (`watchlistId`, `movieId`, `movieTitle`, `movieYear`, `genres`, `runtime`, `isPriority`, `overview`, `posterUrl`)
- `watchedItems`: List of (`watchedId`, `movieId`, `movieTitle`, `movieYear`, `watchedDate`, `userRating`, `hasReview`, `overview`, `posterUrl`)

### Aksi Pengguna
- Berpindah antar tab (`Ulasan Saya`, `Watchlist`, `Sudah Ditonton`).
- Mengetuk tombol `Edit` pada kartu ulasan $\rightarrow$ Membuka bottom sheet `edit-ulasan`.
- Mengetuk tombol `Hapus` pada kartu ulasan $\rightarrow$ Menampilkan dialog konfirmasi hapus ulasan.
- Mengetuk tombol `Bagikan` $\rightarrow$ Membuka share sheet ulasan.
- Mengetuk tombol `Tandai Sudah Ditonton` pada Watchlist $\rightarrow$ Memindahkan film ke daftar Sudah Ditonton.
- Mengetuk FAB `Catat Film` $\rightarrow$ Membuka bottom sheet `tulis-ulasan` atau pencarian film untuk diulas.
- Mengetuk item Bottom Navigation $\rightarrow$ Berpindah tab layar.

### State UI
- **Loading**: Skeleton placeholder untuk kartu metrik bento dan stream daftar kartu ulasan/watchlist.
- **Success**: Seluruh data jurnal dan daftar film ditampilkan sesuai tab aktif.
- **Empty**:
  - Tab Ulasan: "Belum ada ulasan yang ditulis. Ketuk 'Catat Film' untuk memulai."
  - Tab Watchlist: "Watchlist Anda masih kosong."
  - Tab Sudah Ditonton: "Belum ada film yang ditandai selesai ditonton."
- **Error**: Pesan kegagalan memuat data database lokal dengan tombol retry.

---

## 7. Layar Akun & Profil (`akun`)

Layar profil reviewer pribadi, statistik personal sinema, 4 film penentu selera (Top 4 Favorites), grafik distribusi rating, serta pengaturan tema dan database lokal.

### Komponen Utama (Atas ke Bawah)
1. **Top App Bar**:
   - Ikon person berlatar ungu, judul `Akun`, dan subjudul `Profil & Koleksi Pribadi`.
2. **Profile Header Card**:
   - Avatar profil berukuran besar dengan ring ungu dan badge status verifikasi centang.
   - Nama pengguna (`Bagas Pratama`).
   - Label status: Ikon database `Koleksi Sinema Offline`.
   - Tombol pill: `Edit Profil` (ikon pensil).
3. **Section Statistik Personal (2x2 Grid)**:
   - Header `Statistik Personal` dan label `Data Tersimpan`.
   - 4 kartu statistik:
     - `Film Dinilai`: 68 film (ikon bintang).
     - `Ulasan`: 24 ulasan (ikon edit note).
     - `Watchlist`: 18 antrean (ikon bookmark).
     - `Sudah Ditonton`: 72 film (ikon mata/visibility).
4. **Section Film Favorit (Top 4 Cinema Defining Movies)**:
   - Header `Film Favorit` dan subjudul `4 film penentu selera sinema`.
   - Grid 4-kolom poster film vertikal: Poster film dengan badge rating bintang di kanan bawah dan judul film di bawahnya (e.g. *Interstellar* 5.0, *Dune: Part Two* 5.0, *Blade Runner 2049* 4.9, *Parasite* 5.0).
5. **Section Distribusi Rating Pribadi (Rating Distribution Chart)**:
   - Header `Distribusi Rating Pribadi` dan label `68 Film Dinilai`.
   - Bar chart horizontal distribusi dari bintang 5 hingga bintang 1:
     - `5 Bintang`: Bar ungu (47%, 32 film).
     - `4 Bintang`: Bar ungu muda (32%, 22 film).
     - `3 Bintang`: Bar ungu abu (15%, 10 film).
     - `2 Bintang`: Bar ungu tua (5%, 3 film).
     - `1 Bintang`: Bar abu redup (2%, 1 film).
6. **Section Pengaturan (Settings Menu)**:
   - Header `Pengaturan`.
   - Item menu `Tema`: Judul, deskripsi, dan nilai badge `Dark Mode (Ungu Pastel M3)`.
   - Item menu `Bahasa`: Judul, deskripsi, dan nilai badge `Bahasa Indonesia`.
   - Item menu destructive `Reset Data Lokal`: Judul teks merah, deskripsi `Hapus database dan reset ke pengaturan awal`, dan panah kanan merah.
7. **Section Tentang Aplikasi (About App)**:
   - Badge pill `MovieVerse v2.4.0 (Offline Edition)` dan teks sumber data `Data film bersifat demo • Sumber data katalog TMDb`.
8. **Bottom Navigation Bar**:
   - 4 tab navigasi dengan tab `Akun` aktif.

### Data yang Dibutuhkan (Field Names)
- `userProfile`: `userName`, `avatarUrl`, `isVerified`, `syncStatus`
- `userStatistics`: `ratedCount`, `reviewCount`, `watchlistCount`, `watchedCount`
- `favoriteMovies`: List of 4 items (`movieId`, `title`, `userRating`, `posterUrl`)
- `ratingDistribution`: Map of (`starLevel` (1..5) $\rightarrow$ `count`, `percentage`)
- `appSettings`: `themeMode`, `appLanguage`, `appVersion`

### Aksi Pengguna
- Mengetuk tombol `Edit Profil` $\rightarrow$ Membuka dialog edit nama/avatar pengguna.
- Mengetuk poster film favorit $\rightarrow$ Navigasi ke `detail-film`.
- Mengetuk baris pengaturan `Tema` $\rightarrow$ Mengubah preferensi tema aplikasi.
- Mengetuk baris pengaturan `Bahasa` $\rightarrow$ Mengubah preferensi bahasa antarmuka.
- Mengetuk `Reset Data Lokal` $\rightarrow$ Membuka dialog peringatan konfirmasi penghapusan seluruh data lokal.
- Mengetuk item Bottom Navigation $\rightarrow$ Berpindah tab layar.

### State UI
- **Loading**: Skeleton placeholder untuk profil card, grid statistik 2x2, poster favorit, dan grafik distribusi.
- **Success**: Seluruh data profil dan statistik terisi lengkap.
- **Empty**: Jika belum ada film dinilai, grafik distribusi menampilkan nilai nol dan bagian favorit menampilkan slot kosong untuk memilih film favorit.
- **Error**: Menampilkan pesan kesalahan jika terjadi error pembacaan preferensi.

---

## 8. Modal Bottom Sheet: Tulis Ulasan (`tulis-ulasan`)

Layar formulir modal bottom sheet untuk mencatat dan menyimpan ulasan film baru ke dalam penyimpanan lokal.

### Komponen Utama (Atas ke Bawah)
1. **Drag Handle & Header Modal**:
   - Garis pegangan drag sheet di bagian atas tengah.
   - Ikon `rate_review` ungu, judul modal `Tulis Ulasan`.
   - Tombol tutup modal (`close`).
2. **Movie Mini Context Card**:
   - Thumbnail poster vertikal film (*Interstellar*).
   - Info film ringkas: Judul film, badge klasifikasi (`PG-13`), `Tahun • Sutradara • Durasi`, dan skor global TMDb (`8.7 Skor Global`).
3. **Interactive Star Rating Bar**:
   - Label `Rating Anda`.
   - Baris 5 tombol bintang interaktif besar (mendukung kelipatan setengah bintang).
   - Badge angka skor (e.g. `4.5 / 5.0`) dan deskripsi kualitatif (e.g. `(Luar Biasa)`).
   - Keterangan petunjuk: `Ketuk untuk memberi nilai kelipatan 0.5`.
4. **Field Tanggal Menonton**:
   - Label `Tanggal Menonton`.
   - Tombol pemilih tanggal berlatar kontainer dengan ikon kalender, tanggal terpilih (e.g. `12 Mei 2024`), dan tombol `Ubah v`.
5. **Switch Tonton Ulang (Rewatch Toggle)**:
   - Ikon repeat ungu, judul `Tonton Ulang (Rewatch)`, dan subteks deskripsi `Tandai jika Anda menonton film ini lagi`.
   - Toggle switch aktif/nonaktif Material 3.
6. **Multiline Review Text Area**:
   - Label `Catatan & Impresi Pribadi Anda` dan badge `Opsional`.
   - Textarea input catatan (maksimal 1.000 karakter) dengan placeholder deskriptif.
   - Footer textarea: Ikon gembok dengan teks `Tersimpan privat di perangkat` dan penghitung karakter (e.g. `142 / 1.000`).
7. **Quick Mood / Sentiment Chips (Sentimen Tayangan)**:
   - Label `Sentimen Tayangan`.
   - Chip sentimen yang dapat dipilih: `Sinematik Epik` (terpilih dengan ikon check), `Menyentuh Hati` (+), `Soundtrack Terbaik` (+).
8. **Action Buttons Footer**:
   - Tombol utama: `Simpan Ulasan` (ikon check circle, mendukung animasi spinner `Menyimpan...` $\rightarrow$ `Tersimpan!`).
   - Tombol sekunder: `Batal` (teks tombol netral).

---

## 9. Modal Bottom Sheet: Edit Ulasan (`edit-ulasan`)

Layar formulir modal bottom sheet untuk memperbarui atau menghapus ulasan film yang sudah ada di penyimpanan lokal.

### Komponen Utama (Atas ke Bawah)
1. **Drag Handle & Header Modal**:
   - Garis pegangan drag sheet.
   - Judul modal `Edit Ulasan`, badge ID ulasan (e.g. `#MV-2024-88`), dan subjudul `Perbarui catatan penayangan & impresi pribadimu`.
   - Tombol tutup modal (`close`).
2. **Movie Mini Context Card**:
   - Thumbnail poster film, judul film (*Interstellar*), badge tahun (`2014`), dan status indikator `Tersimpan di Jurnal Sinema`.
3. **Interactive Star Rating Bar**:
   - Baris 5 tombol bintang interaktif besar.
   - Label skor numerik (`5.0 / 5.0`) • Predikat kualitatif (`Mahakarya Sinema`).
   - Petunjuk perubahan rating.
4. **Field Tanggal Menonton**:
   - Label `Tanggal Menonton` dengan tanda `Wajib`.
   - Input tanggal menonton aktif (e.g. `05 Agustus 2024`) dan tombol ikon `edit_calendar`.
5. **Switch Tonton Ulang (Rewatch Toggle)**:
   - Ikon replay ungu, judul `Tonton Ulang (Rewatch)`, dan info sesi tontonan (e.g. `Tercatat sebagai sesi menonton ke-4`).
   - Toggle switch interaktif.
6. **Multiline Review Text Area**:
   - Label `Catatan Refleksi & Ulasan` dan penghitung karakter (e.g. `184 / 1.000 karakter`).
   - Textarea terisi ulasan lama yang siap disunting.
   - Footer textarea: Ikon privasi gembok `Tersimpan secara lokal di perangkat` dan ikon format kutipan (`format_quote`).
7. **Action Buttons Area (Primary + Destructive)**:
   - Tombol utama lebar penuh: `Perbarui Ulasan` (ikon check circle).
   - Baris tombol 2-kolom:
     - Tombol destructive: `Hapus Ulasan` (ikon delete dengan warna teks error merah).
     - Tombol netral: `Batal` (tombol abu-abu).

---

## 10. Komparasi Khusus: Tulis Ulasan vs Edit Ulasan

### 10.1 Tabel Perbandingan Konten Antar Layar
| Aspek Desain | Tulis Ulasan (`tulis-ulasan`) | Edit Ulasan (`edit-ulasan`) |
|---|---|---|
| **Judul Header** | `Tulis Ulasan` dengan ikon `rate_review` | `Edit Ulasan` dengan badge ID ulasan (`#MV-2024-88`) & subjudul penjelasan |
| **Subtitle Mini Movie Card** | Menampilkan info sutradara & `8.7 Skor Global TMDb` | Menampilkan tahun & status `Tersimpan di Jurnal Sinema` |
| **Nilai Default Rating** | Kosong atau draft nilai baru (contoh di desain: `4.5 / Luar Biasa`) | Nilai ulasan sebelumnya (contoh di desain: `5.0 / Mahakarya Sinema`) |
| **Deskripsi Rewatch Switch** | Teks umum: *"Tandai jika Anda menonton film ini lagi"* | Teks sesi spesifik: *"Tercatat sebagai sesi menonton ke-4"* |
| **Sentimen Tayangan / Quick Chips** | **Ada**: Kumpulan chip tag sentimen (*Sinematik Epik*, *Menyentuh Hati*, *Soundtrack Terbaik*) | **Tidak ada** (fokus pada penyuntingan teks refleksi dan kuotasi) |
| **Tombol Aksi Utama** | `Simpan Ulasan` | `Perbarui Ulasan` |
| **Tombol Destructive (Hapus)** | **Tidak ada** (hanya tombol Batal) | **Ada**: Tombol `Hapus Ulasan` warna merah berdampingan dengan tombol Batal |

### 10.2 State Formulir yang Harus Disimpan (Form State Specification)
Kedua form mengelola state lokal yang harus dipertahankan saat perubahan input berlangsung:

1. `reviewId`: String/UUID unik (`null` untuk Tulis Ulasan baru, wajib ada untuk Edit Ulasan).
2. `movieId`: ID film target yang sedang diulas.
3. `ratingScore`: Nilai desimal float kelipatan 0.5 dari `0.5` hingga `5.0` (skala 5 bintang).
4. `watchDate`: Tanggal menonton (format tanggal ISO / Epoch millis, direpresentasikan ke teks tanggal e.g. "12 Mei 2024").
5. `isRewatch`: Boolean (`true` jika switch tonton ulang aktif, `false` jika tontonan perdana).
6. `reviewText`: String isi catatan ulasan (panjang $0 \le \text{length} \le 1000$ karakter).
7. `sentimentTags`: Set/List of String tag sentimen terpilih (misal: `["Sinematik Epik", "Soundtrack Terbaik"]`).
8. `formStatus`: State status pengiriman form:
   - `Idle`: Form siap diedit.
   - `Saving`: Tombol menyimpan menampilkan spinner loading.
   - `Saved`: Animasi sukses tersimpan sebelum modal ditutup.
   - `Deleting`: State konfirmasi dan proses hapus (khusus Edit Ulasan).
   - `ValidationError`: Pesan validasi jika data wajib belum terpenuhi.
