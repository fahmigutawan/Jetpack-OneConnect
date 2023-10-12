package com.example.oneconnect.presentation.profile.component

import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel() {
    fun logout(){
        repository.logout()
    }
}