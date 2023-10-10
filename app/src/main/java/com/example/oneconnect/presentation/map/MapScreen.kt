package com.example.oneconnect.presentation.map

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oneconnect.databinding.MapboxViewBinding
import com.example.oneconnect.global_component.AppDialog
import com.example.oneconnect.global_component.AppDialogButtonOrientation
import com.example.oneconnect.ui.theme.seed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController
) {
    val mapView = remember {
        mutableStateOf<MapView?>(null)
    }
    val viewModel = hiltViewModel<MapViewModel>()
    val mapPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (!mapPermission.status.isGranted) {
        if (mapPermission.status.shouldShowRationale) {
            viewModel.showPermissionWarningDialog.value = false
            viewModel.showRationaleDialog.value = true
        } else {
            viewModel.showRationaleDialog.value = false
            viewModel.showPermissionWarningDialog.value = true
        }
    } else {
        viewModel.showPermissionWarningDialog.value = false
        viewModel.showRationaleDialog.value = false
    }

    if (viewModel.showRationaleDialog.value) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Untuk mengakses halaman MAP, pastikan anda sudah mengijinkan akses lokasi dari HP anda",
            secondButton = {
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
            secondButton = {
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

    if (viewModel.useDummyLocation.value == null
        && !viewModel.showPermissionWarningDialog.value
        && !viewModel.showRationaleDialog.value
    ) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Anda bisa menggunakan lokasi realtime, namun bisa jadi obyek darurat tidak terdapat di sekitar lokasi asli anda. Sebagai prototype, anda bisa menggunakan lokasi dummy agar mendapati obyek darurat di sekitar. \n\n *NB. Pilihan ini akan selalu ditanyakan saat anda membuka halaman MAP, jadi jangan khawatir untuk mencoba semua pilihan.",
            secondButton = {
                Button(
                    onClick = {
                        viewModel.useDummyLocation.value = false
                    }
                ) {
                    Text(text = "Gunakan Lokasi Realtime")
                }
            },
            firstButton = {
                Button(
                    onClick = {
                        viewModel.useDummyLocation.value = true
                    }
                ) {
                    Text(text = "Gunakan Lokasi Dummy")
                }
            },
            appDialogButtonOrientation = AppDialogButtonOrientation.VERTICAL
        )
    }

    fusedLocationClient.requestLocationUpdates(
        LocationRequest().apply {
            interval = 2500
            fastestInterval = 1500
        },
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.lastLocation?.let {
                    it.run {
                        viewModel.userLong.value = longitude
                        viewModel.userLat.value = latitude
                    }
                }
            }
        },
        null
    )

    LaunchedEffect(key1 = viewModel.useDummyLocation.value) {
        when (viewModel.useDummyLocation.value) {
            true -> {
                mapView.value?.camera?.flyTo(
                    CameraOptions
                        .Builder()
                        .center(
                            Point.fromLngLat(
                                viewModel.userLong.value,
                                viewModel.userLat.value
                            )
                        )
                        .zoom(15.0)
                        .build()
                )
            }

            false -> {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    viewModel.userLat.value = it.latitude
                    viewModel.userLong.value = it.longitude

                    mapView.value?.camera?.flyTo(
                        CameraOptions
                            .Builder()
                            .center(
                                Point.fromLngLat(
                                    viewModel.userLong.value,
                                    viewModel.userLat.value
                                )
                            )
                            .zoom(15.0)
                            .build()
                    )
                }
            }

            null -> {}
        }
    }

    AndroidView(
        factory = {
            val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

            binding.root
        },
        update = {
            mapView.value = it

            val annotationApi = it.annotations
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            val circleAnnotationOptions = CircleAnnotationOptions()
                .withPoint(
                    Point.fromLngLat(
                        viewModel.userLong.value,
                        viewModel.userLat.value
                    )
                )
                .withCircleRadius(8.0)
                .withCircleColor("#ee4e8b")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeColor("#ffffff")
            circleAnnotationManager.create(circleAnnotationOptions)
        }
    )
}