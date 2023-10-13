package com.example.oneconnect.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneconnect.data.Repository
import com.example.oneconnect.helper.UserDataInputStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun precheck(
        onLoginChecked: (isLogin: Boolean) -> Unit,
        onUserDataInputStatusCheck: (String, UserDataInputStatus) -> Unit
    ) {
        viewModelScope.launch {
            val loginStatus = repository.isLogin()
            delay(2000)
            onLoginChecked(loginStatus)

            if(loginStatus){
                repository.checkUserInputDataStatus(
                    uid = repository.uid(),
                    onSuccess = { phoneNumber, status ->
                        onUserDataInputStatusCheck(phoneNumber, status)
                    },
                    onFailed = {
                        //TODO
                    }
                )
            }
        }
    }
}