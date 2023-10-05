package com.example.oneconnect.global_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oneconnect.R
import com.example.oneconnect.navhost.NavRoutes

data class BottomNavbarItem(
    val route: String,
    val iconId: Int,
    val word: String
)

@Composable
fun BottomNavbar(
    onItemClicked: (route: String) -> Unit,
    currentRoute: String,
    selectedColor: Color,
    unselectedColor: Color
) {
    val items: List<BottomNavbarItem?> = listOf(
        BottomNavbarItem(
            route = NavRoutes.BERANDA.name,
            iconId = R.drawable.navbar_home,
            "Beranda"
        ),
        null,
        BottomNavbarItem(
            route = NavRoutes.PROFIL.name,
            iconId = R.drawable.navbar_profil,
            "Profil"
        )
    )
    val scrWidth = LocalConfiguration.current.screenWidthDp

    BottomAppBar(tonalElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                if (item == null) {
                    Box(modifier = Modifier)
                } else {
                    Box(modifier = Modifier
                        .clickable {
                            onItemClicked(item.route)
                        }
                        .width((scrWidth / 2).dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter(model = item.iconId),
                                contentDescription = "",
                                tint = if (currentRoute == item.route) selectedColor else unselectedColor
                            )
                            Text(
                                text = "Beranda",
                                color = if (currentRoute == item.route) selectedColor else unselectedColor
                            )
                        }
                    }
                }
            }
        }
    }
}