package com.verse.movieverse.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.verse.movieverse.ui.screens.AkunScreen
import com.verse.movieverse.ui.screens.DetailFilmScreen
import com.verse.movieverse.ui.screens.HasilPencarianScreen
import com.verse.movieverse.ui.screens.JelajahScreen
import com.verse.movieverse.ui.screens.JurnalScreen
import com.verse.movieverse.ui.screens.KategoriScreen
import com.verse.movieverse.ui.screens.PencarianAktifScreen

/**
 * Data representasi item tab pada bottom navigation bar.
 */
private data class TopLevelTab(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val topLevelTabs = listOf(
    TopLevelTab(
        route = Jelajah,
        label = "Jelajah",
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore
    ),
    TopLevelTab(
        route = Kategori,
        label = "Kategori",
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category
    ),
    TopLevelTab(
        route = Jurnal,
        label = "Jurnal",
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks
    ),
    TopLevelTab(
        route = Akun,
        label = "Akun",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

/**
 * Navigasi utama aplikasi MovieVerse menggunakan Jetpack Navigation Compose type-safe.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Bottom bar HANYA ditampilkan di 4 tab utama
    val showBottomBar = currentDestination?.let { dest ->
        dest.hasRoute<Jelajah>() ||
        dest.hasRoute<Kategori>() ||
        dest.hasRoute<Jurnal>() ||
        dest.hasRoute<Akun>()
    } ?: true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelTabs.forEach { tab ->
                        // Memeriksa apakah rute tab saat ini aktif
                        val isSelected = currentDestination?.hasRoute(tab.route::class) == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.label
                                )
                            },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // NavHost type-safe dengan 7 rute composable
        NavHost(
            navController = navController,
            startDestination = Jelajah,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 4 Tab Layar Utama
            composable<Jelajah> {
                JelajahScreen(
                    onOpenSearch = { navController.navigate(PencarianAktif) },
                    onOpenDetail = { movieId -> navController.navigate(DetailFilm(movieId)) }
                )
            }
            composable<Kategori> {
                KategoriScreen()
            }
            composable<Jurnal> {
                JurnalScreen()
            }
            composable<Akun> {
                AkunScreen()
            }

            // Layar Non-Tab
            composable<PencarianAktif> {
                PencarianAktifScreen(
                    onSearch = { query -> navController.navigate(HasilPencarian(query)) },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<HasilPencarian> { backStackEntry ->
                val args = backStackEntry.toRoute<HasilPencarian>()
                HasilPencarianScreen(
                    query = args.query,
                    onOpenDetail = { movieId -> navController.navigate(DetailFilm(movieId)) },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<DetailFilm> { backStackEntry ->
                val args = backStackEntry.toRoute<DetailFilm>()
                DetailFilmScreen(
                    movieId = args.movieId,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}
