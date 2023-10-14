package com.example.oneconnect.data

import android.content.Context
import com.example.oneconnect.data.room.RoomDb
import com.example.oneconnect.helper.UserDataInputStatus
import com.example.oneconnect.model.entity.FavoriteItemEntity
import com.example.oneconnect.model.external.MapboxGeocodingResponse
import com.example.oneconnect.model.struct.ContactModel
import com.example.oneconnect.model.struct.EmergencyProviderModel
import com.example.oneconnect.model.struct.EmergencyTypeModel
import com.example.oneconnect.model.struct.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import javax.inject.Inject


class Repository @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val realtimeDb: FirebaseDatabase,
    private val firestore: FirebaseFirestore,
    private val httpClient: HttpClient,
    private val roomDb: RoomDb
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

                        if ((value?.documents?.size ?: 0) > 0) {
                            onSuccess(UserDataInputStatus.INPUTTED)
                            return@addSnapshotListener
                        } else {
                            onSuccess(UserDataInputStatus.HAVE_NOT_INPUTTED)
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
        uid: String,
        onSuccess: (String, UserDataInputStatus) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("user")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                if ((value?.documents?.size ?: 0) > 0) {
                    onSuccess(auth.currentUser?.phoneNumber ?: "", UserDataInputStatus.INPUTTED)
                    return@addSnapshotListener
                } else {
                    onSuccess(
                        auth.currentUser?.phoneNumber ?: "",
                        UserDataInputStatus.HAVE_NOT_INPUTTED
                    )
                    return@addSnapshotListener
                }
            }
    }

    fun saveUserDataInput(
        phoneNumber: String,
        name: String,
        nik: String,
        onSuccess: () -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("user")
            .document(auth.currentUser?.uid ?: "")
            .set(
                UserModel(
                    uid = auth.currentUser?.uid ?: "",
                    name = name,
                    nik = nik,
                    phone_number = phoneNumber,
                    admin = false,
                    created_at = Timestamp.now()
                )
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailed(it)
            }
    }

    fun getAllEmergencyType(
        onSuccess: (List<EmergencyTypeModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("em_type")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        it.documents.map { doc ->
                            EmergencyTypeModel(
                                emTypeId = doc["em_type_id"] as String,
                                word = doc["word"] as String
                            )
                        }
                    )
                    return@addSnapshotListener
                }
            }
    }

    fun getAllEmergencyProvider(
        onSuccess: (List<EmergencyProviderModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("em_srv_provider")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        it.map { doc ->
                            EmergencyProviderModel(
                                em_pvd_id = doc["em_pvd_id"] as String,
                                longitude = doc["longitude"] as String,
                                latitude = doc["latitude"] as String,
                                name = doc["name"] as String,
                                em_type = doc["em_type"] as String
                            )
                        }
                    )
                    return@addSnapshotListener
                }
            }
    }

    fun getAllEmergencyProviderByTypeId(
        emTypeId: String,
        onSuccess: (List<EmergencyProviderModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("em_srv_provider")
            .whereEqualTo("em_type", emTypeId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        it.map { doc ->
                            EmergencyProviderModel(
                                em_pvd_id = doc["em_pvd_id"] as String,
                                longitude = doc["longitude"] as String,
                                latitude = doc["latitude"] as String,
                                name = doc["name"] as String,
                                em_type = doc["em_type"] as String
                            )
                        }
                    )
                    return@addSnapshotListener
                }
            }
    }

    suspend fun getLocationByLongLat(
        longitude: Double,
        latitude: Double,
        onSuccess: (MapboxGeocodingResponse) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        val res = httpClient.get(
            "https://api.mapbox.com/search/geocode/v6/reverse" +
                    "?language=id" +
                    "&longitude=$longitude" +
                    "&latitude=$latitude" +
                    "&access_token=sk.eyJ1IjoiZmFobWlndXRhd2FuIiwiYSI6ImNsbmVwdXAxcjBremEyam1uZGthdXhiMmUifQ.LR6usbmqClTCkQTAHFqFuw"
        )

        try {
            when (res.status) {
                HttpStatusCode.OK -> {
                    onSuccess(res.body())
                }

                else -> {
                    onFailed(java.lang.Exception("Terjadi kesalahan"))
                }
            }
        } catch (e: Exception) {
            onFailed(e)
        }
    }

    fun getContactByProviderId(
        emPvdId: String,
        onSuccess: (List<ContactModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("contact")
            .whereEqualTo("em_pvd_id", emPvdId)
            .orderBy("contact_type")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        it.documents.map { doc ->
                            ContactModel(
                                contact_id = doc["contact_id"] as String,
                                em_pvd_id = doc["em_pvd_id"] as String,
                                number = doc["number"] as String,
                                contact_type = doc["contact_type"] as String
                            )
                        }
                    )
                    return@addSnapshotListener
                }
            }
    }

    fun insertNewFavoriteItem(item: FavoriteItemEntity) =
        roomDb.favoriteItemDao().insertNewFavorite(item)

    fun deleteFavoriteItem(item: FavoriteItemEntity) = roomDb.favoriteItemDao().deleteFavorite(item)

    fun getAllFavoriteItem() = roomDb.favoriteItemDao().getAllFavoriteItem()

    fun logout() {
        auth.signOut()
    }
}