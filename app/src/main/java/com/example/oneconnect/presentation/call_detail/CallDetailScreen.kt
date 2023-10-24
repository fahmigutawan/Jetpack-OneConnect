package com.example.oneconnect.presentation.call_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oneconnect.databinding.MapboxViewBinding
import com.example.oneconnect.global_component.CategoryCardType
import com.example.oneconnect.global_component.EmergencyTypeCard
import com.example.oneconnect.global_component.NonLazyVerticalGrid
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CallDetailScreen(
    navController: NavController,
    emCallId: String
) {
    val viewModel = hiltViewModel<CallDetailViewModel>()
    val mapView = remember {
        mutableStateOf<MapView?>(null)
    }

    LaunchedEffect(key1 = true) {
        viewModel.getCallInfoFromId(emCallId)
    }

    LaunchedEffect(key1 = viewModel.call.value) {
        viewModel.call.value?.let { call ->
            viewModel.getEmProvider(
                emPvdId = call.em_pvd_id ?: ""
            )

            mapView.value?.let {
                it.camera.flyTo(
                    cameraOptions = CameraOptions
                        .Builder()
                        .center(
                            Point.fromLngLat(
                                call.user_long?.toDouble() ?: .0,
                                call.user_lat?.toDouble() ?: .0
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
                            call.user_long?.toDouble() ?: .0,
                            call.user_lat?.toDouble() ?: .0
                        )
                    )
                    .withCircleRadius(8.0)
                    .withCircleColor("#465DFF")
                circleAnnotationManager.create(circleAnnotationOptions)
            }
        }
    }

    Scaffold {
        Box(modifier = Modifier, contentAlignment = Alignment.BottomCenter) {
            AndroidView(
                factory = {
                    val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

                    binding.root
                },
                update = {
                    mapView.value = it
                }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = viewModel.emProvider.value?.name ?: "...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            overflow = TextOverflow.Clip
                        )

                        Text(
                            text = viewModel.emProviderTypeMap[viewModel.emProvider.value?.em_type ?: ""] ?: "...",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            overflow = TextOverflow.Clip
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = viewModel.statusMap[viewModel.call.value?.em_call_status_id ?: ""] ?: "...",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }
    }
}