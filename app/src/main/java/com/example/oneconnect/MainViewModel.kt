package com.example.oneconnect

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.navhost.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel

class MainViewModel:ViewModel() {
    val loading = mutableStateOf(false)
    val currentRoute = mutableStateOf(NavRoutes.SPLASH.name)
    val showBottomBar = mutableStateOf(false)
}