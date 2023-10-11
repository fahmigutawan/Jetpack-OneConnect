package com.example.oneconnect.global_component

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oneconnect.R
import com.example.oneconnect.model.domain.general.PhoneNumberDomain

@Composable
fun ContactInfoCard(
    location: String,
    name: String,
    onCopyClicked: (String) -> Unit,
    onCallClicked: (type: String, number: String) -> Unit,
    copiedNumber: String,
    phoneNumber: List<PhoneNumberDomain>
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

            phoneNumber.forEach { phoneNumber ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = rememberAsyncImagePainter(
                                model = if (phoneNumber.contactType == "wa") R.drawable.icon_whatsapp else R.drawable.icon_phone
                            ),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                        Text(
                            text = phoneNumber.phoneNumber,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Button(
                            onClick = { onCopyClicked(phoneNumber.phoneNumber) },
                            shape = RoundedCornerShape(Int.MAX_VALUE.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (copiedNumber == phoneNumber.phoneNumber) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                contentColor = if (copiedNumber == phoneNumber.phoneNumber) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter(model = if (copiedNumber == phoneNumber.phoneNumber) R.drawable.icon_copied else R.drawable.icon_copy),
                                contentDescription = ""
                            )
                        }

                        Button(
                            onClick = {
                                onCallClicked(
                                    phoneNumber.contactType,
                                    phoneNumber.phoneNumber
                                )
                            },
                            shape = RoundedCornerShape(Int.MAX_VALUE.dp)
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter(model = R.drawable.icon_call),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}