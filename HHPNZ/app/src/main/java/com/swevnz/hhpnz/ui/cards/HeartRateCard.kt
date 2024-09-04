package com.swevnz.hhpnz.ui.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swevnz.hhpnz.data.model.HeartRate
import java.text.SimpleDateFormat
import androidx.compose.foundation.lazy.items
import java.util.*

@Composable
fun HeartRateCard(
    latestHeartRate: HeartRate?,
    allHeartRates: List<HeartRate>,
    title: String = "Heart Rate",
    unit: String = "bpm"
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        HeartRateDetailsDialog(
            allHeartRates = allHeartRates,
            onDismiss = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.headlineMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                        ) {
                            append(latestHeartRate?.bpm?.toString() ?: "0")
                        }
                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                            append(" $unit")
                        }
                    }
                )
                Text(
                    text = latestHeartRate?.timestamp?.let { formatTimestamp(it) }
                        ?: "Today",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun HeartRateDetailsDialog(
    allHeartRates: List<HeartRate>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Heart Rates ❤\uFE0F",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                if (allHeartRates.isNotEmpty()) {
                    LazyColumn {
                        items(allHeartRates) { heartRate ->
                            HeartRateDetailItem(heartRate)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                } else {
                    Text("No heart rate data available.")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
@Composable
fun HeartRateDetailItem(heartRate: HeartRate) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(heartRate.timestamp)

    Column {
        Text("BPM: ${heartRate.bpm}")
        Text("Date: $formattedDate")
    }
}

@Composable
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun HeartRateCardPreview() {
    HeartRateCard(
        latestHeartRate = HeartRate(
            id = "1",
            bpm = 72,
            timestamp = System.currentTimeMillis(),
            date = System.currentTimeMillis()
        ),
        allHeartRates = listOf(
            HeartRate(id = "1", bpm = 72, timestamp = System.currentTimeMillis(), date = System.currentTimeMillis()),
            HeartRate(id = "2", bpm = 68, timestamp = System.currentTimeMillis() - 60000, date = System.currentTimeMillis() - 60000)
        )
    )
}
