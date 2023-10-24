package com.example.oneconnect.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneconnect.data.Repository
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeEmergencyTypeDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain
import com.example.oneconnect.model.entity.FavoriteItemEntity
import com.example.oneconnect.model.struct.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val copiedNumber = mutableStateOf("")
    val emTypes = mutableStateListOf<HomeEmergencyTypeDomain>()
    val favoritePhoneProviders = mutableStateListOf<FavoriteItemEntity>()
    val availableTransportCountMaps = mutableStateOf(mapOf<String, Int>())
    val userInfo = mutableStateOf<UserModel?>(null)

    fun deleteFavoriteItem(item: FavoriteItemEntity) = repository.deleteFavoriteItem(item)

    fun getAllFavorites() = viewModelScope.launch(Dispatchers.IO) {
        val list = async {
            repository.getAllFavoriteItem()
        }

        favoritePhoneProviders.addAll(list.await())
    }

    fun getMultipleTransportCount(
        emPvdIds:List<String>
    ){
        repository.getMultipleTransportCount(
            emPvdIds = emPvdIds,
            onSuccess = {
                availableTransportCountMaps.value = it
                Log.e("ALSKDJLKAS", it.toString())
            },
            onFailed = {
                //TODO
                Log.e("LASKDJLASK", it.toString())
            }
        )
    }

    init {
        repository.getAllEmergencyType(
            onSuccess = {
                emTypes.clear()
                emTypes.addAll(
                    it.map { model ->
                        HomeEmergencyTypeDomain(
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

        repository.getUserInfo(
            onSuccess = {
                userInfo.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.listenEmCallSnapshot()
    }
}