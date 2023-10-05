package com.example.oneconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.oneconnect.global_component.BottomNavbar
import com.example.oneconnect.navhost.AppNavHost
import com.example.oneconnect.navhost.NavRoutes
import com.example.oneconnect.ui.theme.OneConnectTheme
import dagger.hilt.android.AndroidEntryPoint

lateinit var mainViewModel: MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val _mainViewModel by viewModels<MainViewModel>()
        mainViewModel = _mainViewModel

        setContent {
            val navController = rememberNavController()
            val selectedNavbarItemColor = Color.Blue
            val unselectedNavbarItemColor = Color.Gray

            navController.addOnDestinationChangedListener { _, destination, _ ->
                mainViewModel.currentRoute.value = destination.route ?: NavRoutes.SPLASH.name

                when (destination.route) {
                    NavRoutes.BERANDA.name, NavRoutes.MAP.name, NavRoutes.PROFIL.name -> mainViewModel.showBottomBar.value =
                        true

                    else -> mainViewModel.showBottomBar.value = false
                }
            }

            OneConnectTheme {
                Scaffold(
                    bottomBar = {
                        if (mainViewModel.showBottomBar.value) {
                            BottomNavbar(
                                onItemClicked = {
                                    navController.navigate(it)
                                },
                                currentRoute = mainViewModel.currentRoute.value,
                                selectedColor = selectedNavbarItemColor,
                                unselectedColor = unselectedNavbarItemColor
                            )
                        }
                    },
                    floatingActionButton = {
                        if (mainViewModel.showBottomBar.value) {
                            Column(
                                modifier = Modifier.offset(y = 64.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                FloatingActionButton(
                                    shape = CircleShape,
                                    onClick = {
                                        navController.navigate(NavRoutes.MAP.name)
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "",
                                        tint = if (mainViewModel.currentRoute.value == NavRoutes.MAP.name) selectedNavbarItemColor else unselectedNavbarItemColor
                                    )
                                }
                                Text(
                                    text = "Maps",
                                    color = if (mainViewModel.currentRoute.value == NavRoutes.MAP.name) selectedNavbarItemColor else unselectedNavbarItemColor
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) {
                    AppNavHost(
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                        navController = navController
                    )
                }
            }
        }
    }
}