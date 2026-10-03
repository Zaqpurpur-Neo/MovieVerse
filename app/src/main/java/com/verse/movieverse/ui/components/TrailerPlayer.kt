package com.verse.movieverse.ui.components

import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Komponen pemutar trailer YouTube menggunakan WebView.
 *
 * Mengapa loadDataWithBaseURL dipakai (bukan loadUrl atau loadData):
 * YouTube embed menolak request tanpa referrer yang valid (Error 153).
 * Dengan base URL https, browser mengirim header Referer yang diterima YouTube.
 *
 * Mengapa referrerpolicy="strict-origin-when-cross-origin":
 * Mengirim origin (domain) tanpa path penuh, memenuhi syarat keamanan YouTube
 * sekaligus menjaga privasi pengguna.
 *
 * Mengapa WebView dilepas (destroy) saat onRelease:
 * WebView memakan memori besar dan memegang koneksi; jika tidak dilepas
 * saat composable keluar dari komposisi, memori bocor dan video terus
 * memutar di latar belakang.
 */
@Composable
fun TrailerPlayer(
    trailerId: String,
    modifier: Modifier = Modifier
) {
    // key(trailerId) memastikan WebView dibuat ulang bila film berganti,
    // sehingga video lama berhenti dan video baru dimuat dari awal.
    key(trailerId) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    webViewClient = WebViewClient()
                    setBackgroundColor(Color.BLACK)
                }
            },
            update = { webView ->
                val html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body { margin: 0; padding: 0; background: #000; }
                            .video-container { position: relative; width: 100%; height: 100%; }
                            iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }
                        </style>
                    </head>
                    <body>
                        <div class="video-container">
                            <iframe
                                src="https://www.youtube.com/embed/$trailerId?playsinline=1&rel=0&fs=0"
                                frameborder="0"
                                referrerpolicy="strict-origin-when-cross-origin"
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                allowfullscreen>
                            </iframe>
                        </div>
                    </body>
                    </html>
                """.trimIndent()

                // Base URL https wajib agar YouTube menerima request (mencegah Error 153).
                webView.loadDataWithBaseURL(
                    "https://zaqpurpur-neo.github.io/movieverse-data/",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            },
            onRelease = { webView ->
                // Lepas WebView saat composable tidak lagi ditampilkan.
                webView.stopLoading()
                webView.destroy()
            }
        )
    }
}