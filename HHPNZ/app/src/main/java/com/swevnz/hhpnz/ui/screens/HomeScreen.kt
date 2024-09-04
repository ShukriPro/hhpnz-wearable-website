package com.swevnz.hhpnz.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swevnz.hhpnz.data.HealthDataDB
import com.swevnz.hhpnz.data.viewModel.HomeViewModel
import com.swevnz.hhpnz.ui.cards.ExerciseCard
import com.swevnz.hhpnz.ui.cards.HeartRateCard
import com.swevnz.hhpnz.ui.cards.StepsCard
import com.swevnz.hhpnz.ui.components.ActivityScreen
import com.swevnz.hhpnz.ui.components.BottomNavigationComponent
import com.swevnz.hhpnz.ui.components.DateComponent
import com.swevnz.hhpnz.ui.theme.HHPNZTheme

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val database = HealthDataDB.getDatabase(context.applicationContext)
    val viewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    heartRateDao = database.heartRateDao(),
                    stepsDao = database.stepsDao(),
                    sleepDao = database.sleepDao(),
                    activeCaloriesBurnedDao = database.activeCaloriesBurnedDao(),
                    weightDao = database.weightDao(),
                    exerciseDao = database.exerciseDao(),
                    distanceDao = database.distanceDao(),
                    vo2MaxDao = database.vo2MaxDao()
                )

            }
        }
    )

    Scaffold(
        bottomBar = {
            BottomNavigationComponent()
        }
    ) { paddingValues ->
        val latestHeartRate by viewModel.latestHeartRate.observeAsState()
        val allHeartRates by viewModel.allHeartRates.observeAsState(emptyList())

        val latestSteps by viewModel.latestSteps.observeAsState()
        val allSteps by viewModel.allSteps.observeAsState(emptyList())

        val latestSleepRecord by viewModel.latestSleepRecord.observeAsState()
        val allSleepRecords by viewModel.allSleepRecords.observeAsState(emptyList())

        val latestActiveCaloriesBurned by viewModel.latestActiveCaloriesBurned.observeAsState()
        val allActiveCaloriesBurned by viewModel.allActiveCaloriesBurned.observeAsState(emptyList())

        val latestWeight by viewModel.latestWeight.observeAsState()
        val allWeights by viewModel.allWeights.observeAsState(emptyList())

        val latestExercise by viewModel.latestExercise.observeAsState()
        val allExercises by viewModel.allExercises.observeAsState(emptyList())

        val latestDistance by viewModel.latestDistance.observeAsState()
        val allDistances by viewModel.allDistances.observeAsState(emptyList())

        val latestVo2Max by viewModel.latestVo2Max.observeAsState()
        val allVo2MaxRecords by viewModel.allVo2MaxRecords.observeAsState(emptyList())


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(10.dp), // Combine padding into a single modifier
            verticalArrangement = Arrangement.spacedBy(15.dp) // Spacing between items
        ) {
            item {
                DateComponent()
            }

            item {
                HeartRateCard(
                    latestHeartRate = latestHeartRate,
                    allHeartRates = allHeartRates
                )
            }

            item {
                StepsCard(
                    latestSteps = latestSteps,
                    allSteps = allSteps
                )
            }


            item {
                ActivityScreen()
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HHPNZTheme {
        HomeScreen()
    }
}
