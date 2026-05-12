//
//package com.example.kavyakanaja
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AutoStories
//import androidx.compose.material.icons.filled.Headphones
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.School
//import androidx.compose.material.icons.filled.ShowChart
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.kavyakanaja.screens.BhavarthaScreen
//import com.example.kavyakanaja.screens.FavouritesScreen
//import com.example.kavyakanaja.screens.FillBlankScreen
//import com.example.kavyakanaja.screens.LearnHubScreen
//import com.example.kavyakanaja.screens.ListenLearnScreen
//import com.example.kavyakanaja.screens.OnboardingManager
//import com.example.kavyakanaja.screens.OnboardingScreen
//import com.example.kavyakanaja.screens.PoemOfDayScreen
//import com.example.kavyakanaja.screens.PoetCornerScreen
//import com.example.kavyakanaja.screens.ProgressScreen
//import com.example.kavyakanaja.screens.QuizScreen
//import com.example.kavyakanaja.screens.SplashScreen
//import com.example.kavyakanaja.ui.theme.KavyaKanajaTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        StreakManager.updateStreak(this)
//        setContent {
//            KavyaKanajaTheme {
//                MainScreen()
//            }
//        }
//    }
//}
//
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//    val context = androidx.compose.ui.platform.LocalContext.current
//
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    // Bottom bar hidden on splash, onboarding and sub-screens
//    val bottomNavRoutes = listOf("poem", "listen", "learn", "poet", "progress")
//    val showBottomBar   = currentRoute in bottomNavRoutes
//
//    val navItems = listOf(
//        Triple("poem",     Icons.Filled.AutoStories, "Poem"),
//        Triple("listen",   Icons.Filled.Headphones,  "Listen"),
//        Triple("learn",    Icons.Filled.School,       "Learn"),
//        Triple("poet",     Icons.Filled.Person,       "Poets"),
//        Triple("progress", Icons.Filled.ShowChart,    "Progress")
//    )
//
//    Scaffold(
//        bottomBar = {
//            if (showBottomBar) {
//                NavigationBar(
//                    containerColor = Color(0xFF1A0A00),
//                    tonalElevation = 0.dp
//                ) {
//                    navItems.forEach { (route, icon, label) ->
//                        val selected = currentRoute == route
//                        NavigationBarItem(
//                            icon     = { Icon(icon, contentDescription = label) },
//                            label    = { Text(label) },
//                            selected = selected,
//                            onClick  = {
//                                navController.navigate(route) {
//                                    popUpTo(navController.graph.startDestinationId)
//                                    launchSingleTop = true
//                                }
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor   = Color(0xFF1A0A00),
//                                selectedTextColor   = Color(0xFFFFD700),
//                                unselectedIconColor = Color(0x88DEB887),
//                                unselectedTextColor = Color(0x88DEB887),
//                                indicatorColor      = Color(0xFFFFD700)
//                            )
//                        )
//                    }
//                }
//            }
//        }
//    ) { innerPadding ->
//        NavHost(
//            navController    = navController,
//            startDestination = "splash",          // ← always starts with splash
//            modifier         = Modifier.padding(innerPadding)
//        ) {
//
//            // ── Splash — always first, decides where to go next ───
//            composable("splash") {
//                SplashScreen(
//                    onFinished = {
//                        val next = if (OnboardingManager.isCompleted(context))
//                            "poem" else "onboarding"
//                        navController.navigate(next) {
//                            // Remove splash from back stack —
//                            // pressing back won't return to it
//                            popUpTo("splash") { inclusive = true }
//                        }
//                    }
//                )
//            }
//
//            // ── Onboarding ────────────────────────────────────────
//            composable("onboarding") {
//                OnboardingScreen(
//                    onFinished = {
//                        navController.navigate("poem") {
//                            popUpTo("onboarding") { inclusive = true }
//                        }
//                    }
//                )
//            }
//
//            // ── 5 main tabs ───────────────────────────────────────
//            composable("poem")     { PoemOfDayScreen() }
//            composable("listen")   { ListenLearnScreen() }
//            composable("poet")     { PoetCornerScreen() }
//            composable("progress") { ProgressScreen() }
//
//            // ── Learn Hub ─────────────────────────────────────────
//            composable("learn") {
//                LearnHubScreen(onNavigate = { route -> navController.navigate(route) })
//            }
//
//            // ── Sub-screens (no bottom bar) ───────────────────────
//            composable("quiz")       { QuizScreen() }
//            composable("fillblank")  { FillBlankScreen() }
//            composable("bhavartha")  { BhavarthaScreen() }
//            composable("favourites") { FavouritesScreen() }
//        }
//    }
//}









package com.example.kavyakanaja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kavyakanaja.screens.BhavarthaScreen
import com.example.kavyakanaja.screens.FavouritesScreen
import com.example.kavyakanaja.screens.FillBlankScreen
import com.example.kavyakanaja.screens.LearnHubScreen
import com.example.kavyakanaja.screens.ListenLearnScreen
import com.example.kavyakanaja.screens.OnboardingManager
import com.example.kavyakanaja.screens.OnboardingScreen
import com.example.kavyakanaja.screens.PoemOfDayScreen
import com.example.kavyakanaja.screens.PoetCornerScreen
import com.example.kavyakanaja.screens.ProgressScreen
import com.example.kavyakanaja.screens.QuizScreen
import com.example.kavyakanaja.screens.SplashScreen
import com.example.kavyakanaja.ui.theme.KavyaKanajaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Setup on every app open ───────────────────────────────
        StreakManager.updateStreak(this)
        NotificationHelper.createChannel(this)   // ← safe to call every time

        setContent {
            KavyaKanajaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf("poem", "listen", "learn", "poet", "progress")
    val showBottomBar   = currentRoute in bottomNavRoutes

    val navItems = listOf(
        Triple("poem",     Icons.Filled.AutoStories, "Poem"),
        Triple("listen",   Icons.Filled.Headphones,  "Listen"),
        Triple("learn",    Icons.Filled.School,       "Learn"),
        Triple("poet",     Icons.Filled.Person,       "Poets"),
        Triple("progress", Icons.Filled.ShowChart,    "Progress")
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF1A0A00),
                    tonalElevation = 0.dp
                ) {
                    navItems.forEach { (route, icon, label) ->
                        val selected = currentRoute == route
                        NavigationBarItem(
                            icon     = { Icon(icon, contentDescription = label) },
                            label    = { Text(label) },
                            selected = selected,
                            onClick  = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = Color(0xFF1A0A00),
                                selectedTextColor   = Color(0xFFFFD700),
                                unselectedIconColor = Color(0x88DEB887),
                                unselectedTextColor = Color(0x88DEB887),
                                indicatorColor      = Color(0xFFFFD700)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "splash",
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
                    onFinished = {
                        val next = if (OnboardingManager.isCompleted(context))
                            "poem" else "onboarding"
                        navController.navigate(next) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            composable("onboarding") {
                OnboardingScreen(
                    onFinished = {
                        navController.navigate("poem") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
            composable("poem")     { PoemOfDayScreen() }
            composable("listen")   { ListenLearnScreen() }
            composable("poet")     { PoetCornerScreen() }
            composable("progress") { ProgressScreen() }
            composable("learn") {
                LearnHubScreen(onNavigate = { route -> navController.navigate(route) })
            }
            composable("quiz")       { QuizScreen() }
            composable("fillblank")  { FillBlankScreen() }
            composable("bhavartha")  { BhavarthaScreen() }
            composable("favourites") { FavouritesScreen() }
        }
    }
}