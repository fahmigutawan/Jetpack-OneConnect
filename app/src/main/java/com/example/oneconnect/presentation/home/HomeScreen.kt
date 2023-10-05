package com.example.oneconnect.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oneconnect.global_component.ContactInfoCard
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain

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

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dummyFavoriteNumber) { item ->
            ContactInfoCard(
                address = item.location,
                name = item.name,
                phoneNumber = item.numbers
            )
        }
    }
}