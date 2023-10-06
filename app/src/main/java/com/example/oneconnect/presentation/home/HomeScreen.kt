package com.example.oneconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oneconnect.global_component.CategoryCard
import com.example.oneconnect.global_component.CategoryCardType
import com.example.oneconnect.global_component.ContactInfoCard
import com.example.oneconnect.global_component.LastCallCard
import com.example.oneconnect.global_component.NonLazyVerticalGrid
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeCategoryDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain
import com.example.oneconnect.model.domain.home.HomeLastCallDomain
import com.example.oneconnect.model.domain.home.HomeUserDomain
import com.example.oneconnect.presentation.home.component.HomeProfileSection

@Composable
fun HomeScreen(
    navController: NavController
) {
    val dummyFavoriteNumber = listOf(
        HomeFavoriteNumberDomain(
            id = "",
            name = "RS Bhayangkara",
            location = "Nganjuk",
            numbers = listOf(
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "wa"
                ),
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "reg"
                )
            )
        ),
        HomeFavoriteNumberDomain(
            id = "",
            name = "RS Bhayangkara",
            location = "Nganjuk",
            numbers = listOf(
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "wa"
                ),
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "reg"
                )
            )
        ),
        HomeFavoriteNumberDomain(
            id = "",
            name = "RS Bhayangkara",
            location = "Nganjuk",
            numbers = listOf(
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "wa"
                ),
                PhoneNumberDomain(
                    phoneNumber = "0812345678",
                    contactType = "reg"
                )
            )
        )
    )
    val dummyLastCall = HomeLastCallDomain(
        id = "123",
        name = "RS Permata Indah",
        location = "Dinoyo, Malang",
        status = "Menunggu Konfirmasi"
    )
    val dummyCategories = listOf(
        HomeCategoryDomain(
            "1",
            "Ambulans"
        ),
        HomeCategoryDomain(
            "2",
            "Damkar"
        ),
        HomeCategoryDomain(
            "3",
            "SAR"
        ),
        HomeCategoryDomain(
            "4",
            "Polisi"
        )
    )
    val dummyProfile = HomeUserDomain(
        name = "Fahmi Noordin Rumagutawan",
        address = "Jl. Kalpataru"
    )

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeProfileSection(modifier = Modifier.padding(top = 16.dp),name = dummyProfile.name, location = dummyProfile.address)
        }
        item {
            Text(text = "Panggilan Terakhir")
        }
        item {
            LastCallCard(
                name = dummyLastCall.name,
                location = dummyLastCall.location,
                status = dummyLastCall.status
            )
        }
        item {
            Text(text = "Nomor Favorit")
        }
        item {
            val scrWidth = LocalConfiguration.current.screenWidthDp
            NonLazyVerticalGrid(
                modifier = Modifier,
                containerWidth = (scrWidth - 32).dp,
                columnCount = 2,
                verticalSpacing = 4.dp
            ) {
                dummyCategories.forEach {
                    item {
                        CategoryCard(
                            modifier = Modifier.padding(4.dp),
                            type = CategoryCardType.BIG,
                            word = it.word,
                            onClick = {

                            }
                        )
                    }
                }
            }
        }
        item {
            Text(text = "Nomor Favorit")
        }
        items(dummyFavoriteNumber) { item ->
            ContactInfoCard(
                location = item.location,
                name = item.name,
                phoneNumber = item.numbers
            )
        }
    }
}