package com.example.oneconnect.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oneconnect.R
import com.example.oneconnect.databinding.MapboxViewBinding
import com.example.oneconnect.global_component.AppDialog
import com.example.oneconnect.global_component.AppDialogButtonOrientation
import com.example.oneconnect.global_component.CategoryCard
import com.example.oneconnect.global_component.CategoryCardType
import com.example.oneconnect.global_component.NonLazyVerticalGrid
import com.example.oneconnect.helper.SnackbarHandler
import com.example.oneconnect.mainViewModel
import com.example.oneconnect.model.domain.home.HomeCategoryDomain
import com.example.oneconnect.ui.theme.seed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.location2
import kotlin.system.exitProcess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController
) {
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

    val mapView = remember {
        mutableStateOf<MapView?>(null)
    }
    val viewModel = hiltViewModel<MapViewModel>()
    val mapPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val myLocationIcon = remember {
        mutableStateOf(Icons.Default.GpsFixed)
    }
    val onMoveListener = object : OnMoveListener {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveBegin(detector: MoveGestureDetector) {
            myLocationIcon.value = Icons.Default.GpsNotFixed
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}

    }

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

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    LaunchedEffect(key1 = viewModel.useDummyLocation.value) {
        when (viewModel.useDummyLocation.value) {
            true -> {
                mapView.value?.let {
                    it.camera.flyTo(
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
                        .withCircleColor("#465DFF")
                    circleAnnotationManager.create(circleAnnotationOptions)
                }
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

                mapView.value?.let {
                    it.location.updateSettings {
                        this.enabled = true
                        this.locationPuck = LocationPuck2D(
                            bearingImage = context.resources.getDrawable(
                                R.drawable.ic_circle,
                            ),
                            scaleExpression = interpolate {
                                linear()
                            }.toJson()
                        )
                    }
                    it.getMapboxMap().addOnMoveListener(onMoveListener)
                }
            }

            null -> {}
        }
    }

    BackHandler {
        if(mainViewModel.backClicked.value){
            exitProcess(0)
        }else{
            SnackbarHandler.showSnackbar("Klik kembali sekali lagi untuk keluar dari OneConnect")
            mainViewModel.backClicked.value = true
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                myLocationIcon.value = Icons.Default.GpsFixed
                mapView.value?.let {
                    it.camera.flyTo(
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
            }) {
                Icon(imageVector = myLocationIcon.value, contentDescription = "")
            }
        }
    ) {
        Box(modifier = Modifier) {
            AndroidView(
                factory = {
                    val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

                    binding.root
                },
                update = {
                    mapView.value = it
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(Color.White)
            ) {
                NonLazyVerticalGrid(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    columnCount = 2,
                    containerHorizontalPadding = 8.dp
                ) {
                    dummyCategories.forEach {
                        item {
                            Box(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                CategoryCard(
                                    onClick = { /*TODO*/ },
                                    type = CategoryCardType.SMALL,
                                    word = it.word
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}