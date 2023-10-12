package com.example.oneconnect.model.domain.map

import com.example.oneconnect.model.domain.general.PhoneNumberDomain

data class MapPickedNumberDomain(
    val id:String,
    val name:String,
    val location:String,
    val numbers:List<PhoneNumberDomain>
)
