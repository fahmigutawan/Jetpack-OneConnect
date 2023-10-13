package com.example.oneconnect.presentation.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.oneconnect.R
import com.example.oneconnect.helper.UserDataInputStatus
import com.example.oneconnect.navhost.NavRoutes

@Composable
fun SplashScreen(navController: NavController) {
    val viewmodel = hiltViewModel<SplashViewModel>()

    viewmodel.precheck(
        onLoginChecked = { login ->
            if(!login){
                navController.navigate(NavRoutes.LOGIN.name) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        },
        onUserDataInputStatusCheck = { phoneNumber, status ->
            when(status){
                UserDataInputStatus.INPUTTED -> {
                    navController.navigate(NavRoutes.BERANDA.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                UserDataInputStatus.HAVE_NOT_INPUTTED -> {
                    navController.navigate("${NavRoutes.USER_DATA_INPUT.name}/$phoneNumber") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier.padding(horizontal = 64.dp),
            model = R.drawable.splash_logo,
            contentDescription = ""
        )
    }
}