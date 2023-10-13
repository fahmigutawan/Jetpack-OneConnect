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
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
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
    fun provideHttpClient() = HttpClient(Android){
        install(ContentNegotiation){
            gson()
        }

        install(HttpTimeout){
            requestTimeoutMillis = 10000
        }

        install(Logging)
    }

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        auth: FirebaseAuth,
        realtimeDb: FirebaseDatabase,
        firestore: FirebaseFirestore,
        httpClient: HttpClient
    ) = Repository(
        context = context,
        auth = auth,
        realtimeDb = realtimeDb,
        firestore = firestore,
        httpClient = httpClient
    )
}