package com.swevnz.hhpnz.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swevnz.hhpnz.data.model.Activity
import com.swevnz.hhpnz.data.model.HeartRate
import com.swevnz.hhpnz.data.model.Steps
import com.swevnz.hhpnz.data.viewModel.ActivityViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(viewModel: ActivityViewModel = viewModel()) {
    var selectedActivityType by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchLatestActivitiesForAllTypes()
    }

    val latestActivities by viewModel.latestActivities.observeAsState(emptyList())
    val displayedActivities = viewModel.ensureAllMetricsPresent(latestActivities)

    Column(modifier = Modifier.padding()) {
        displayedActivities.forEach { activity ->
            ActivityCard(
                title = viewModel.formatType(activity.type),
                value = activity.value.toString(),
                unit = when (activity.type) {
                    "heart_rate" -> "bpm"
                    "steps" -> "steps"
                    "sleep" -> "hours"
                    "active calories" -> "cal"
                    "exercise" -> "min"
                    "distance" -> "km"
                    "vo2max" -> "mL/kg/min"
                    "weight" -> "kg"
                    else -> ""
                },
                subtext = "Today",
                icon = getIconForActivityType(activity.type),
                onClick = { selectedActivityType = activity.type }
            )
            Spacer(Modifier.height(16.dp))
        }
    }

    selectedActivityType?.let { activityType ->
        ActivityDetailsDialog(
            activityType = activityType,
            viewModel = viewModel,
            onDismiss = { selectedActivityType = null }
        )
    }
}

@Composable
fun getIconForActivityType(type: String): ImageVector {
    return when (type) {
        "steps" -> Icons.Filled.Person
        "distance" -> Icons.Filled.LocationOn
        "energy_burned" -> Icons.Filled.Favorite
        else -> Icons.Filled.Person // Default icon
    }
}

@Composable
fun ActivityCard(
    title: String,
    value: String,
    unit: String,
    subtext: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            Modifier
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
                            append(value)
                        }
                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                            append(" $unit")
                        }
                    }
                )
                Text(text = subtext, fontSize = 14.sp, color = Color.Gray)
            }
//            Icon(
//                imageVector = icon,
//                contentDescription = title,
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(48.dp)
//            )
        }
    }
}

@Composable
fun ActivityDetailsDialog(
    activityType: String,
    viewModel: ActivityViewModel,
    onDismiss: () -> Unit
) {
    val detailedActivities by viewModel.detailedActivities.collectAsState()

    LaunchedEffect(activityType) {
        viewModel.fetchDetailedActivities(activityType)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${viewModel.formatType(activityType)} Details",
                style = MaterialTheme.typography.titleMedium // Smaller title text
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .height(300.dp) // Fixed height for the dialog content
                    .fillMaxWidth()
            ) {
                LazyColumn {
                    items(detailedActivities) { activity ->
                        ActivityDetailItem(activity)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
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
fun ActivityDetailItem(activity: Activity) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(activity.date)

    Column {
        Text("Value: ${activity.value}")
        Text("Date: ${formattedDate}")
        Text("Source: ${activity.source}")
    }
}