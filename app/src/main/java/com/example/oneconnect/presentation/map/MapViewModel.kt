package com.example.oneconnect.presentation.map

import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){
}