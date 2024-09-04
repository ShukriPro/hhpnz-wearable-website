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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swevnz.hhpnz.data.model.Exercise
import java.text.SimpleDateFormat
import androidx.compose.foundation.lazy.items
import java.util.*

@Composable
fun ExerciseCard(
    latestExercise: Exercise?,
    allExercises: List<Exercise>,
    title: String = "Exercise",
    unit: String = "min" // Assuming the duration is in minutes
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ExerciseDetailsDialog(
            allExercises = allExercises,
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
                            append((latestExercise?.duration?.div(60000))?.toString() ?: "0")

                        }
                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                            append(" $unit")
                        }
                    }
                )
                Text(
                    text = latestExercise?.timestamp?.let { formatTimestamp(it) }
                        ?: "Today",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ExerciseDetailsDialog(
    allExercises: List<Exercise>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Exercise 🏋️‍♂️",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                if (allExercises.isNotEmpty()) {
                    LazyColumn {
                        items(allExercises) { exercise ->
                            ExerciseDetailItem(exercise)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                } else {
                    Text("No exercise data available.")
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
fun ExerciseDetailItem(exercise: Exercise) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(exercise.timestamp)

    Column {
        Text("Type: ${exercise.type}")
        Text("Duration: ${exercise.duration / 60000} min")
        Text("Date: $formattedDate")
    }
}

