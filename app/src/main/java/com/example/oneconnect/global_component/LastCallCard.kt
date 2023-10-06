package com.example.oneconnect.global_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LastCallCard(
    name: String,
    location: String,
    status: String
) {
    val scrWidth = LocalConfiguration.current.screenWidthDp

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.widthIn(max = (scrWidth / 2).dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = location, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
            Column(
                modifier = Modifier.widthIn(max = (scrWidth / 2).dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = status, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Lihat Detail")
                }
            }
        }
    }
}