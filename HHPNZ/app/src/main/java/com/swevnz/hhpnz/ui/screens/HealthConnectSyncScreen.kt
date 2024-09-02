package com.swevnz.hhpnz.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swevnz.hhpnz.HealthConnectManager
import com.swevnz.hhpnz.data.HealthDataDB
import com.swevnz.hhpnz.data.HealthRepository
import kotlinx.coroutines.launch

@Composable
fun HealthConnectSyncScreen() {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val healthConnectManager = remember { HealthConnectManager(context) }
    val activityDao = remember { HealthDataDB.getDatabase(context).activityDao() }
    val healthRepository = remember {
        HealthRepository(
            healthConnectManager = healthConnectManager,
            activityDao = activityDao,
            heartRateDao = HealthDataDB.getDatabase(context).heartRateDao(),
            stepsDao = HealthDataDB.getDatabase(context).stepsDao(),
            sleepDao = HealthDataDB.getDatabase(context).sleepDao(),
            activeCaloriesBurnedDao = HealthDataDB.getDatabase(context).activeCaloriesBurnedDao(),
            weightDao = HealthDataDB.getDatabase(context).weightDao(),
            exerciseDao = HealthDataDB.getDatabase(context).exerciseDao(),
            distanceDao = HealthDataDB.getDatabase(context).distanceDao(),
            vo2MaxDao = HealthDataDB.getDatabase(context).vo2MaxDao()
        )
    }

    val coroutineScope = rememberCoroutineScope()

    // Remember launcher for activity result to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = healthConnectManager.requestPermissionsActivityContract()
    ) { granted ->
        coroutineScope.launch {
            if (granted.containsAll(healthConnectManager.permissions)) {
                Log.d("Permissions", "All permissions granted")
                healthRepository.syncHealthData() // Call in coroutine
                // Go to LoginScreen
                activity?.setContent { LoginScreen() }
            } else {
                Log.d("Permissions", "Some permissions denied")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title and description
        Text(
            text = "Sync HHPNZ with Health Connect",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "To help you stay on top of all your goals, HHPNZ can now sync with Health Connect to share data with the other apps that you use.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "This means that you can add data to HHPNZ from your other apps and devices, and share your Fit data with them.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Section "How it works"
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "How it works",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "When you set up Health Connect, you can choose which data you want HHPNZ to read or write.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "When you allow HHPNZ to read a data type from Health Connect, it’s stored in your Google Account with your other HHPNZ data.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "HHPNZ uses this data to provide features, like tracking your fitness and health and giving related recommendations and insights.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Button(
                onClick = {
                    permissionLauncher.launch(healthConnectManager.permissions)
                }
            ) {
                Text("Set up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthConnectSyncScreenPreview() {
    HealthConnectSyncScreen()
}

