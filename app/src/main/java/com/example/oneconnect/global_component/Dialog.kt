package com.example.oneconnect.global_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    rightButton:@Composable (() -> Unit)? = null,
    leftButton:@Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    title: String? = null,
    properties: DialogProperties? = null,
    description: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                leftButton?.let {
                    it()
                }
                rightButton?.let {
                    it()
                }
            }
        },
        title = {
            title?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(text = description)
        },
        shape = RoundedCornerShape(8.dp),
        properties = properties ?: DialogProperties()

    )
//    Dialog(
//        onDismissRequest = onDismissRequest,
//        properties = properties ?: DialogProperties()
//    ) {
//        val containerWidth = remember {
//            mutableStateOf(0.dp)
//        }
//        val density = LocalDensity.current
//
//        Box(modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color.White)){
//            Column(
//                modifier = modifier
//                    .padding(16.dp)
//                    .onSizeChanged {
//                        with(density) {
//                            containerWidth.value = it.width.toDp()
//                        }
//                    }
//                    .background(Color.Gray),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                title?.let {
//                    Text(
//                        modifier = Modifier.width(containerWidth.value),
//                        text = it,
//                        textAlign = TextAlign.Center
//                    )
//                }
//                Text(text = description)
//                buttons?.let {
//                    it(containerWidth.value)
//                }
//            }
//        }
//    }
}