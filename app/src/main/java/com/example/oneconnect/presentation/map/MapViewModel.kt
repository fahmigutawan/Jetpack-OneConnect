package com.example.oneconnect.presentation.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){
    val showRationaleDialog = mutableStateOf(false)
    val showPermissionWarningDialog = mutableStateOf(false)
    val useDummyLocation = mutableStateOf<Boolean?>(null)

    val userLong = mutableStateOf(112.63092731744476)
    val userLat = mutableStateOf(-7.945945945945946)
}