package com.example.oneconnect.helper

import com.example.oneconnect._showSnackbar
import com.example.oneconnect._showSnackbarWithAction


class SnackbarHandler {
    companion object{
        fun showSnackbar (
            message: String
        ) {
            _showSnackbar(message)
        }

        fun showSnackbarWithAction(
            message: String,
            actionLabel:String = "Tutup",
            action:() -> Unit
        ){
            _showSnackbarWithAction(
                message,
                actionLabel,
                action
            )
        }
    }
}