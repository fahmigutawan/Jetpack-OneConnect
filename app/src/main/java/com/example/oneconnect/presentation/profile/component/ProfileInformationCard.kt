package com.example.oneconnect.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oneconnect.R

@Composable
fun ProfileInformationCard(
    phoneNumber: String,
    nik: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Informasi Pribadi", style = MaterialTheme.typography.headlineSmall)
            Column {
                Text(text = "Nomor HP", style = MaterialTheme.typography.titleMedium)
                Text(text = phoneNumber)
            }
            Column {
                Text(text = "NIK", style = MaterialTheme.typography.titleMedium)
                Text(text = nik)
            }
        }
    }
}