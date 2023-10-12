package com.example.oneconnect.presentation.otp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import com.example.oneconnect.helper.UserDataInputStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val otpCode = mutableStateOf("")
    val verificationId = mutableStateOf("")

    val resendCountdown = mutableStateOf(60)

    fun sendOtp(options: (auth: FirebaseAuth) -> PhoneAuthOptions) {
        repository.sendOtp(options = options)
    }

    fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: (UserDataInputStatus) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        repository.signInWithCredential(credential, onSuccess, onFailed)
    }
}