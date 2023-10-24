package com.example.oneconnect.model.struct

import com.google.firebase.Timestamp

data class CallModel(
    val em_call_id:String? = "",
    val uid:String? = "",
    val em_transport_id:String? = "",
    val em_pvd_id:String? = "",
    val user_long:String? = "",
    val user_lat:String? = "",
    val transport_long:String? = "",
    val transport_lat:String? = "",
    val created_at:Map<String, String>,
    val user_phone_number:String? = "",
    val em_call_status_id:String? = ""
)