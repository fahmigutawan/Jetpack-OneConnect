package com.example.oneconnect.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oneconnect.data.Repository
import com.example.oneconnect.model.domain.general.PhoneNumberDomain
import com.example.oneconnect.model.domain.home.HomeEmergencyTypeDomain
import com.example.oneconnect.model.domain.home.HomeFavoriteNumberDomain
import com.example.oneconnect.model.entity.FavoriteItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val copiedNumber = mutableStateOf("")
    val emTypes = mutableStateListOf<HomeEmergencyTypeDomain>()
    val favoritePhoneProviders = mutableStateListOf<FavoriteItemEntity>()

    fun insertNewFavoriteItem(item: FavoriteItemEntity) = repository.insertNewFavoriteItem(item)

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

        favoritePhoneProviders.addAll(repository.getAllFavoriteItem())
    }
}