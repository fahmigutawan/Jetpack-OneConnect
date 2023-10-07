package com.example.oneconnect.presentation.map

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oneconnect.databinding.MapboxViewBinding
import com.example.oneconnect.global_component.AppDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<MapViewModel>()
    val mapPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri

    if (!mapPermission.status.isGranted) {
        if (mapPermission.status.shouldShowRationale) {
            viewModel.showPermissionWarningDialog.value = false
            viewModel.showRationaleDialog.value = true
        } else {
            viewModel.showRationaleDialog.value = false
            viewModel.showPermissionWarningDialog.value = true
        }
    }

    if (viewModel.showRationaleDialog.value) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Untuk mengakses halaman MAP, pastikan anda sudah mengijinkan akses lokasi dari HP anda",
            rightButton = {
                Button(
                    onClick = {
                        mapPermission.launchPermissionRequest()
                    }
                ) {
                    Text(text = "Ijinkan")
                }
            }
        )
    }

    if (viewModel.showPermissionWarningDialog.value) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Sepertinya anda harus mengijinkan akses lokasi secara manual, klik tombol di bawah ini!",
            rightButton = {
                Button(
                    onClick = {
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "Buka setting")
                }
            }
        )
    }

    AndroidView(
        factory = {
            val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

            binding.root
        },
        update = {

        }
    )
}