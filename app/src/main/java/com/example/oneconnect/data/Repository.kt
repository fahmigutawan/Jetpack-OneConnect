package com.example.oneconnect.data

import android.content.Context
import android.util.Log
import com.example.oneconnect.data.room.RoomDb
import com.example.oneconnect.helper.UserDataInputStatus
import com.example.oneconnect.model.entity.FavoriteItemEntity
import com.example.oneconnect.model.external.MapboxGeocodingResponse
import com.example.oneconnect.model.struct.CallModel
import com.example.oneconnect.model.struct.CallStatusModel
import com.example.oneconnect.model.struct.ContactModel
import com.example.oneconnect.model.struct.EmergencyProviderModel
import com.example.oneconnect.model.struct.EmergencyTypeModel
import com.example.oneconnect.model.struct.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import java.util.UUID
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

    fun getEmergencyProviderById(
        emPvdId: String,
        onSuccess: (EmergencyProviderModel) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("em_srv_provider")
            .document(emPvdId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let { doc ->
                    onSuccess(
                        EmergencyProviderModel(
                            em_pvd_id = doc["em_pvd_id"] as String,
                            longitude = doc["longitude"] as String,
                            latitude = doc["latitude"] as String,
                            name = doc["name"] as String,
                            em_type = doc["em_type"] as String
                        )
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

    fun getMultipleTransportCount(
        emPvdIds: List<String>,
        onSuccess: (Map<String, Int>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        val mapResult = mutableMapOf<String, Int>()
        emPvdIds.forEach { pvd_id ->
            firestore
                .collection("em_transport")
                .whereEqualTo("em_pvd_id", pvd_id)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        onFailed(error)
                    }

                    mapResult[pvd_id] = value?.documents?.filter {
                        it["is_available"] as Boolean
                    }?.size ?: 0

                    if (mapResult.size == emPvdIds.size) {
                        onSuccess(mapResult)
                    }
                }
        }
    }

    fun getSingleTransportCount(
        emPvdId: String,
        onSuccess: (Int) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("em_transport")
            .whereEqualTo("em_pvd_id", emPvdId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                }

                value?.let {
                    onSuccess(
                        value.documents.filter {
                            it["is_available"] as Boolean
                        }.size
                    )
                }
            }
    }

    fun getUserInfo(
        uid: String = auth.currentUser?.uid ?: "",
        onSuccess: (UserModel) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        firestore
            .collection("user")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailed(error)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        UserModel(
                            uid = it["uid"] as String,
                            name = it["name"] as String,
                            nik = it["nik"] as String,
                            phone_number = it["phone_number"] as String,
                            admin = it["admin"] as Boolean,
                            created_at = it["created_at"] as Timestamp
                        )
                    )
                }
            }
    }

    fun makeCallObjectInRealtimeDb(
        emPvdId: String,
        userLong: Double,
        userLat: Double
    ) {
        val em_call_id = UUID.randomUUID().toString()
        val uid = auth.currentUser?.uid ?: ""
        val phoneNumber = auth.currentUser?.phoneNumber ?: ""
        val em_transport_id = "." //This is default value
        val transport_long = "." //This is default value
        val transport_lat = "." //This is default value
        val em_call_status_id = "S6LQDRJKurqtVAmRgBqy" //This is default value
        val created_at = ServerValue.TIMESTAMP

        val body = CallModel(
            em_call_id = em_call_id,
            uid = uid,
            em_transport_id = em_transport_id,
            em_pvd_id = emPvdId,
            user_long = userLong.toString(),
            user_lat = userLat.toString(),
            transport_long = transport_long,
            transport_lat = transport_lat,
            user_phone_number = phoneNumber,
            created_at = created_at,
            em_call_status_id = em_call_status_id
        )

        realtimeDb
            .reference
            .child("em_call")
            .child(em_call_id)
            .setValue(body)
    }

    fun listenEmCallSnapshot(
        uid: String = auth.currentUser?.uid ?: "",
        onListened: (List<CallModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        realtimeDb
            .reference
            .child("em_call")
            .orderByChild("uid")
            .equalTo(uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val sorted = snapshot.children.sortedBy {
                            it.child("created_at").value as Long
                        }

                        onListened(
                            sorted.map {
                                CallModel(
                                    em_call_id = it.child("em_call_id").value as String,
                                    uid = it.child("uid").value as String,
                                    em_transport_id = it.child("em_transport_id").value as String,
                                    em_pvd_id = it.child("em_pvd_id").value as String,
                                    user_long = it.child("user_long").value as String,
                                    user_lat = it.child("user_lat").value as String,
                                    transport_long = it.child("transport_long").value as String,
                                    transport_lat = it.child("transport_lat").value as String,
                                    user_phone_number = it.child("user_phone_number").value as String,
                                    em_call_status_id = it.child("em_call_status_id").value as String,
                                )
                            }
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailed(error.toException())
                    }
                }
            )
    }

    fun getAllCallStatus(
        onSuccess: (List<CallStatusModel>) -> Unit,
        onFailed: (Exception) -> Unit
    ){
        firestore
            .collection("em_call_status")
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailed(it)
                    return@addSnapshotListener
                }

                value?.let {
                    onSuccess(
                        it.documents.map {
                            CallStatusModel(
                                em_call_status_id = it["em_call_status_id"] as String,
                                word = it["word"] as String
                            )
                        }
                    )
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}