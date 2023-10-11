package com.example.oneconnect.presentation.login

import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
}