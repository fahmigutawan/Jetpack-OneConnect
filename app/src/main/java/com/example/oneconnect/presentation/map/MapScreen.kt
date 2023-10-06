package com.example.oneconnect.presentation.map

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.oneconnect.R
import com.example.oneconnect.databinding.MapboxViewBinding

@Composable
fun MapScreen() {
    val viewModel = hiltViewModel<MapViewModel>()

    AndroidView(
        factory = {
            val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

            binding.root
        },
        update = {

        }
    )
}