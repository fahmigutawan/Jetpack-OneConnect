package com.example.oneconnect.global_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oneconnect.R
import com.example.oneconnect.helper.ContactTypeIcon
import com.example.oneconnect.model.domain.general.PhoneNumberDomain

@Composable
fun ContactInfoCard(
    location: String,
    name: String,
    onCopyClicked: (String) -> Unit,
    onCallClicked: (type: String, number: String) -> Unit,
    copiedNumber: String,
    phoneNumber: List<PhoneNumberDomain>,
    onDeleteClick: () -> Unit,
) {
    val maxLocationWidth = LocalConfiguration.current.screenWidthDp / 3
    val maxNameWidth = LocalConfiguration.current.screenWidthDp * 2 / 3
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onDeleteClick()
                    },
                    text = "Hapus dari Favorit",
                    color = MaterialTheme.colorScheme.error,
                    textDecoration = TextDecoration.Underline
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.widthIn(max = maxNameWidth.dp),
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier.widthIn(max = maxLocationWidth.dp),
                        text = location,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            phoneNumber.forEach { phoneNumber ->
                SingleContactItem(
                    onCopyClicked = onCopyClicked,
                    onCallClicked = onCallClicked,
                    copiedNumber = copiedNumber,
                    phoneNumber = phoneNumber
                )
            }
        }
    }
}