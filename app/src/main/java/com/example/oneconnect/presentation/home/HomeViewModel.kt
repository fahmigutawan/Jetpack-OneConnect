package com.example.oneconnect.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneconnect.data.Repository
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeEmergencyTypeDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain
import com.example.oneconnect.model.entity.FavoriteItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val copiedNumber = mutableStateOf("")
    val emTypes = mutableStateListOf<HomeEmergencyTypeDomain>()
    val favoritePhoneProviders = MutableStateFlow<MutableList<FavoriteItemEntity>>(
        mutableStateListOf()
    )

    fun deleteFavoriteItem(item: FavoriteItemEntity) = repository.deleteFavoriteItem(item)

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

        viewModelScope.launch(Dispatchers.IO) {
            val list = async {
                repository.getAllFavoriteItem()
            }

            favoritePhoneProviders.value.addAll(list.await())
        }
    }
}