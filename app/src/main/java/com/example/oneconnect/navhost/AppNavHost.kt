package com.example.oneconnect.navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oneconnect.MainViewModel
import com.example.oneconnect.presentation.home.HomeScreen
import com.example.oneconnect.presentation.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavRoutes.SPLASH.name
    ) {
        composable(NavRoutes.SPLASH.name){
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.BERANDA.name){
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.MAP.name){

        }

        composable(NavRoutes.PROFIL.name){

        }
    }
}