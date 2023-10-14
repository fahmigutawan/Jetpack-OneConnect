package com.example.oneconnect.data.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.google.gson.Gson

@ProvidedTypeConverter
class RoomConverters {
    @TypeConverter
    fun numbersListToJson(numbers: List<PhoneNumberDomain>):String {
        val listTmp = numbers.map { singlePhoneNumber ->
            Gson().toJson(singlePhoneNumber)
        }
        return listTmp.joinToString(
            separator = ",",
            prefix = "",
            postfix = ""
        )
    }

    @TypeConverter
    fun numbersJsonToList(numbersJson: String):List<PhoneNumberDomain> {
        val listTmp = numbersJson.split(",")
        return listTmp.map { Gson().fromJson(it, PhoneNumberDomain::class.java) }
    }
}