package com.example.oneconnect.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.oneconnect.R
import com.example.oneconnect.model.struct.FcmTokenStruct
import com.google.android.gms.cloudmessaging.CloudMessage
import com.google.android.gms.cloudmessaging.CloudMessagingReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseFCMService : FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var builder = NotificationCompat.Builder(this, "CHANNEL ID")
            .setSmallIcon(R.drawable.splash_logo)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSound(Uri.parse(message.notification?.sound))
            .setPriority(NotificationCompat.PRIORITY_MAX)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CHANNEL NAME"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL ID", name, importance)
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(
            0,
            builder.build()
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
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