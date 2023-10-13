package com.example.oneconnect.presentation.map

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import com.example.oneconnect.model.domain.home.HomeEmergencyTypeDomain
import com.example.oneconnect.model.domain.map.MapEmergencyProviderDomain
import com.example.oneconnect.model.domain.map.MapEmergencyTypeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){
    val showRationaleDialog = mutableStateOf(false)
    val showPermissionWarningDialog = mutableStateOf(false)
    val useDummyLocation = mutableStateOf<Boolean?>(null)

    val userLong = mutableStateOf(112.6150769)
    val userLat = mutableStateOf(-7.9540899)

    val emTypes = mutableStateListOf<MapEmergencyTypeDomain>()
    val emProviders = mutableStateListOf<MapEmergencyProviderDomain>()

    val pickedEmergencyProvider = mutableStateOf<MapEmergencyProviderDomain?>(null)

    val pickedEmTypeId = mutableStateOf("")

    fun getAllEmergencyProvider(){
        repository.getAllEmergencyProvider(
            onSuccess = {
                emProviders.clear()
                emProviders.addAll(
                    it.map { model ->
                        MapEmergencyProviderDomain(
                            em_pvd_id = model.em_pvd_id.toString(),
                            em_type = model.em_type.toString(),
                            name = model.name.toString(),
                            longitude = model.longitude.toString().toDouble(),
                            latitude = model.latitude.toString().toDouble()
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    fun getAllEmergencyProviderByTypeId(
        emTypeId:String
    ){
        repository.getAllEmergencyProviderByTypeId(
            emTypeId = emTypeId,
            onSuccess = {
                emProviders.clear()
                emProviders.addAll(
                    it.map { model ->
                        MapEmergencyProviderDomain(
                            em_pvd_id = model.em_pvd_id.toString(),
                            em_type = model.em_type.toString(),
                            name = model.name.toString(),
                            longitude = model.longitude.toString().toDouble(),
                            latitude = model.latitude.toString().toDouble()
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    init {
        repository.getAllEmergencyType(
            onSuccess = {
                emTypes.clear()
                emTypes.addAll(
                    it.map { model ->
                        MapEmergencyTypeDomain(
                            emTypeId = model.emTypeId ?: "",
                            word = model.word ?: ""
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }
}