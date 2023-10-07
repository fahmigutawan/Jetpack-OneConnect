package com.example.oneconnect.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oneconnect.R

@Composable
fun ProfileTopSection(
    modifier: Modifier,
    name: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(108.dp)
                .clip(CircleShape),
            model = R.drawable.icon_dummy_pp,
            contentDescription = ""
        )
        Text(text = name)
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Edit Profil")
            Icon(imageVector = Icons.Default.Edit, contentDescription = "")
        }
    }
}