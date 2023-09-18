package com.moment.studywithme

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moment.studywithme.ui.screens.home.HomeScreen
import com.moment.studywithme.ui.screens.home.chill.BreakViewModel
import com.moment.studywithme.ui.screens.home.pomodoro.PomodoroViewModel
import com.moment.studywithme.ui.screens.splash.SplashScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Splash : Screen("splash")
}

@Composable
fun SWMApp(
    application: SWMApplication,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(
                pomodoroViewModel = viewModel(
                    factory = PomodoroViewModel.provideFactory(application)
                ),
                breakViewModel = viewModel(
                    factory = BreakViewModel.provideFactory(application)
                )
            )
        }
    }
}
