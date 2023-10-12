package com.example.oneconnect.helper

import com.example.oneconnect.mainViewModel


object LoadingHandler{
    fun loading() {
        mainViewModel.loading.value = true
    }

    fun dismiss() {
        mainViewModel.loading.value = false
    }
}