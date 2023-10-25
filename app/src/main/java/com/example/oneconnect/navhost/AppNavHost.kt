package com.example.oneconnect.navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.oneconnect.MainViewModel
import com.example.oneconnect.presentation.call_detail.CallDetailScreen
import com.example.oneconnect.presentation.history.HistoryScreen
import com.example.oneconnect.presentation.home.HomeScreen
import com.example.oneconnect.presentation.login.LoginScreen
import com.example.oneconnect.presentation.map.MapScreen
import com.example.oneconnect.presentation.otp.OtpScreen
import com.example.oneconnect.presentation.profile.ProfileScreen
import com.example.oneconnect.presentation.splash.SplashScreen
import com.example.oneconnect.presentation.user_data_input.UserDataInputScreen

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
        composable(NavRoutes.SPLASH.name) {
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.BERANDA.name) {
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.MAP.name) {
            MapScreen(navController = navController)
        }

        composable(NavRoutes.PROFIL.name) {
            ProfileScreen(navController = navController)
        }

        composable(NavRoutes.LOGIN.name) {
            LoginScreen(navController = navController)
        }

        composable(
            route = "${NavRoutes.OTP.name}/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                }
            )
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber") ?: ""

            OtpScreen(
                phoneNumber = phoneNumber,
                navController = navController
            )
        }

        composable(
            route = "${NavRoutes.USER_DATA_INPUT.name}/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                }
            )
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber") ?: ""

            UserDataInputScreen(phoneNumber = phoneNumber, navController = navController)
        }

        composable(
            route = "${NavRoutes.MAP.name}/{em_type_id}",
            arguments = listOf(
                navArgument("em_type_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val emTypeId = it.arguments?.getString("em_type_id") ?: ""

            MapScreen(
                navController = navController,
                emTypeId = emTypeId
            )
        }

        composable(
            route = "${NavRoutes.CALL_DETAIL.name}/{em_call_id}",
            arguments = listOf(
                navArgument("em_call_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val emCallId = it.arguments?.getString("em_call_id") ?: ""

            CallDetailScreen(
                navController = navController,
                emCallId = emCallId
            )
        }

        composable(
            route = NavRoutes.HISTORY.name
        ){
            HistoryScreen(navController = navController)
        }
    }
}