package com.example.oneconnect

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.model.struct.FcmTokenStruct
import com.example.oneconnect.navhost.NavRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel

class MainViewModel : ViewModel() {
    val loading = mutableStateOf(false)
    val currentRoute = mutableStateOf(NavRoutes.SPLASH.name)
    val showBottomBar = mutableStateOf(false)
    val backClicked = mutableStateOf(false)

    init {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val fcm = FirebaseMessaging.getInstance()

        fcm.token.addOnSuccessListener { token ->
            firestore
                .collection("fcm_token")
                .document(auth.currentUser?.uid ?: "")
                .set(
                    FcmTokenStruct(
                        uid = auth.currentUser?.uid ?: "",
                        token = token
                    )
                )
        }
    }
}