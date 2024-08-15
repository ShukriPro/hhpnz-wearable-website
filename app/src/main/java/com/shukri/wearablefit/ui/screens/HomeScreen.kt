package com.shukri.wearablefit.ui.screens

import NutritionTrackerComponent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shukri.wearablefit.ui.components.ActivityComponent
import com.shukri.wearablefit.ui.components.InfoComponent
import com.shukri.wearablefit.ui.components.MealLogSummaryComponent
import com.shukri.wearablefit.ui.components.BottomNavigationComponent
import com.shukri.wearablefit.viewmodel.HealthViewModel
import androidx.compose.material3.Scaffold

@Composable
fun HomeScreen(
    healthViewModel: HealthViewModel = viewModel()
) {
    val latestStep by healthViewModel.latestStep.collectAsState()
    val latestSleep by healthViewModel.latestSleep.collectAsState()
    val latestWeight by healthViewModel.latestWeight.collectAsState()
    val latestHeartRate by healthViewModel.latestHeartRate.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationComponent() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoComponent()

            NutritionTrackerComponent(
                carbsCurrent = 44,
                carbsTotal = 316,
                proteinCurrent = 10,
                proteinTotal = 126,
                fatCurrent = 4,
                fatTotal = 84
            )

            ActivityComponent(
                steps = latestStep?.count ?: 0,
                sleepDuration = latestSleep?.durationMinutes ?: 0,
                weight = latestWeight?.weightKg ?: 0.0,
                heartRate = latestHeartRate?.bpm?.toInt() ?: 0
            )

            MealLogSummaryComponent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Scaffold(
        bottomBar = { BottomNavigationComponent() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            InfoComponent()

            NutritionTrackerComponent(
                carbsCurrent = 44,
                carbsTotal = 316,
                proteinCurrent = 10,
                proteinTotal = 126,
                fatCurrent = 4,
                fatTotal = 84
            )

            ActivityComponent(
                steps = 8500,
                sleepDuration = 450,
                weight = 70.5,
                heartRate = 72
            )

            MealLogSummaryComponent()
        }
    }
}
