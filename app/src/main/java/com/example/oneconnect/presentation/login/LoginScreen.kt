package com.example.oneconnect.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.oneconnect.R
import com.example.oneconnect.navhost.NavRoutes
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<LoginViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 128.dp),
            model = R.drawable.splash_logo,
            contentDescription = ""
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Masuk", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Hai, silahkan masukkan nomor untuk melanjutkan.")
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Text(text = "+62", color = Color.DarkGray)
                },
                value = viewModel.phoneNumber.value,
                onValueChange = {
                    viewModel.phoneNumber.value = it
                },
                label = {
                    Text(text = "Nomor Telepon")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 128.dp),
            onClick = {
                navController.navigate("${NavRoutes.OTP.name}/+62${viewModel.phoneNumber.value}")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            enabled = android.util.Patterns.PHONE.matcher("+62${viewModel.phoneNumber.value}").matches()
        ) {
            Text(text = "Lanjutkan", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}