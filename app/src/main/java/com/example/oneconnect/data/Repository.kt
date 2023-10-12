package com.example.oneconnect.data

import android.app.Activity
import android.content.Context
import com.example.oneconnect.helper.UserDataInputStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class Repository @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val realtimeDb: FirebaseDatabase,
    private val firestore: FirebaseFirestore
) {
    fun sendOtp(options: (auth: FirebaseAuth) -> PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(options(auth))
    }

    fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: (UserDataInputStatus) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        auth
            .signInWithCredential(credential)
            .addOnSuccessListener {
                firestore
                    .collection("user")
                    .whereEqualTo("uid", it.user?.uid ?: "")
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            onFailed(error)
                            return@addSnapshotListener
                        }

                        if((value?.documents?.size ?: 0) > 0){
                            onSuccess(UserDataInputStatus.INPUTTED)
                            return@addSnapshotListener
                        }
                    }
            }.addOnFailureListener {
                onFailed(it)
            }
    }

    fun isLogin() = auth.currentUser != null

    fun uid() = auth.currentUser?.uid ?: ""

    fun checkUserInputDataStatus(
        uid:String,
        onSuccess: (UserDataInputStatus) -> Unit,
        onFailed: (Exception) -> Unit
    ){
        firestore
            .collection("user")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                if((value?.documents?.size ?: 0) > 0){
                    onSuccess(UserDataInputStatus.INPUTTED)
                    return@addSnapshotListener
                }
            }
    }

    fun logout(){
        auth.signOut()
    }
}