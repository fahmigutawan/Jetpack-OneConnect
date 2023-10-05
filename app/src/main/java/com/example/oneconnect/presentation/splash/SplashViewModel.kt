package com.example.oneconnect.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneconnect.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){
    fun precheck(onCheck:() -> Unit){
        viewModelScope.launch {
            delay(2000)
            onCheck()
        }
    }
}