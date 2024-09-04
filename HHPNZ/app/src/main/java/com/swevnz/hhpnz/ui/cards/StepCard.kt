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
import com.swevnz.hhpnz.data.model.Steps
import java.text.SimpleDateFormat
import androidx.compose.foundation.lazy.items
import java.util.*

@Composable
fun StepsCard(
    latestSteps: Steps?,
    allSteps: List<Steps>,
    title: String = "Steps",
    unit: String = "steps"
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        StepsDetailsDialog(
            allSteps = allSteps,
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
                            append(latestSteps?.stepCount?.toString() ?: "0")
                        }
                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                            append(" $unit")
                        }
                    }
                )
                Text(
                    text = latestSteps?.timestamp?.let { formatTimestamp(it) }
                        ?: "Today",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun StepsDetailsDialog(
    allSteps: List<Steps>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Steps 🚶‍♂️",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                if (allSteps.isNotEmpty()) {
                    LazyColumn {
                        items(allSteps) { steps ->
                            StepsDetailItem(steps)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                } else {
                    Text("No steps data available.")
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
fun StepsDetailItem(steps: Steps) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(steps.timestamp)

    Column {
        Text("Steps: ${steps.stepCount}")
        Text("Date: $formattedDate")
    }
}


@Preview(showBackground = true)
@Composable
fun StepsCardPreview() {
    StepsCard(
        latestSteps = Steps(
            id = "1",
            stepCount = 10000,
            timestamp = System.currentTimeMillis(),
            date = System.currentTimeMillis()
        ),
        allSteps = listOf(
            Steps(id = "1", stepCount = 10000, timestamp = System.currentTimeMillis(), date = System.currentTimeMillis()),
            Steps(id = "2", stepCount = 8000, timestamp = System.currentTimeMillis() - 60000, date = System.currentTimeMillis() - 60000)
        )
    )
}
