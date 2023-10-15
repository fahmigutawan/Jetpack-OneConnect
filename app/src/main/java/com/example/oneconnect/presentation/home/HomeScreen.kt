package com.example.oneconnect.presentation.home

import android.content.Intent
import android.net.Uri
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oneconnect.global_component.EmergencyTypeCard
import com.example.oneconnect.global_component.CategoryCardType
import com.example.oneconnect.global_component.ContactInfoCard
import com.example.oneconnect.global_component.LastCallCard
import com.example.oneconnect.global_component.NonLazyVerticalGrid
import com.example.oneconnect.helper.SnackbarHandler
import com.example.oneconnect.mainViewModel
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain
import com.example.oneconnect.model.domain.home.HomeLastCallDomain
import com.example.oneconnect.model.domain.home.HomeUserDomain
import com.example.oneconnect.navhost.NavRoutes
import com.example.oneconnect.presentation.home.component.HomeProfileSection
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    navController: NavController
) {
    val dummyLastCall = HomeLastCallDomain(
        id = "123",
        name = "RS Permata Indah",
        location = "Dinoyo, Malang",
        status = "Menunggu Konfirmasi"
    )
    val dummyProfile = HomeUserDomain(
        name = "Fahmi Noordin Rumagutawan",
        address = "Jl. Kalpataru"
    )

    val viewModel = hiltViewModel<HomeViewModel>()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    BackHandler {
        if (mainViewModel.backClicked.value) {
            exitProcess(0)
        } else {
            SnackbarHandler.showSnackbar("Klik kembali sekali lagi untuk keluar dari OneConnect")
            mainViewModel.backClicked.value = true
        }
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeProfileSection(
                modifier = Modifier.padding(top = 16.dp),
                name = dummyProfile.name,
                location = dummyProfile.address
            )
        }
        item {
            Text(text = "Panggilan Terakhir", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            LastCallCard(
                name = dummyLastCall.name,
                location = dummyLastCall.location,
                status = dummyLastCall.status
            )
        }
        item {
            Text(text = "Layanan Darurat", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            val scrWidth = LocalConfiguration.current.screenWidthDp
            NonLazyVerticalGrid(
                modifier = Modifier,
                containerWidth = (scrWidth - 32).dp,
                columnCount = 2,
                verticalSpacing = 4.dp
            ) {
                viewModel.emTypes.forEach {
                    item {
                        EmergencyTypeCard(
                            modifier = Modifier.padding(4.dp),
                            type = CategoryCardType.BIG,
                            id = it.emTypeId,
                            word = it.word,
                            onClick = {
                                navController.navigate(
                                    "${NavRoutes.MAP.name}/${it.emTypeId}"
                                )
                            }
                        )
                    }
                }
            }
        }
        item {
            Text(text = "Nomor Favorit", style = MaterialTheme.typography.headlineSmall)
        }
        if (viewModel.favoritePhoneProviders.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Belum Ada Nomor Favorit",
                    textAlign = TextAlign.Center
                )
            }
        }
        items(viewModel.favoritePhoneProviders) { item ->
            ContactInfoCard(
                location = item.location ?: "",
                name = item.em_pvd_name ?: "",
                phoneNumber = item.numbers.data,
                onCallClicked = { type, number ->
                    when (type) {
                        "wa" -> {
                            val numFix =
                                "https://api.whatsapp.com/send?phone=${number.replace("+", "")}}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(numFix))
                            context.startActivity(intent)
                        }

                        else -> {
                            val uri = Uri.parse("tel:$number")
                            val intent = Intent(Intent.ACTION_DIAL, uri)
                            context.startActivity(intent)
                        }
                    }
                },
                onCopyClicked = {
                    clipboardManager.setText(AnnotatedString(it))
                    viewModel.copiedNumber.value = it
                    SnackbarHandler.showSnackbar("Nomor telah di-copy")
                },
                copiedNumber = viewModel.copiedNumber.value,
                onDeleteClick = {
                    viewModel.deleteFavoriteItem(item)
                    viewModel.favoritePhoneProviders.remove(item)
                    SnackbarHandler.showSnackbar("Berhasil dihapus dari favorit")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}