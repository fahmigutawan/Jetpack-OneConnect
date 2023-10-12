package com.example.oneconnect.di

import android.content.Context
import com.example.oneconnect.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRealtimeDb() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        auth: FirebaseAuth,
        realtimeDb: FirebaseDatabase,
        firestore: FirebaseFirestore
    ) = Repository(
        context = context,
        auth = auth,
        realtimeDb = realtimeDb,
        firestore = firestore
    )
}