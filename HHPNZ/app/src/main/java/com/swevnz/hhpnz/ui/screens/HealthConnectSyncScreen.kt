package com.swevnz.hhpnz.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
    val healthRepository = remember {
        HealthRepository(
            healthConnectManager = healthConnectManager,
            activityDao = HealthDataDB.getDatabase(context).activityDao(),
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

    var agreementChecked by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = healthConnectManager.requestPermissionsActivityContract()
    ) { granted ->
        coroutineScope.launch {
            if (granted.containsAll(healthConnectManager.permissions)) {
                healthRepository.syncHealthData()
                activity?.setContent { LoginScreen() }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,

    ) {
        Text(
            text = "Privacy Agreement",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "To ensure a seamless and integrated experience, HHPNZ requires access to various health data through Health Connect. By allowing these permissions, HHPNZ can securely sync your health metrics with other apps you use, providing a comprehensive view of your fitness and well-being.\n\n" +
                        "Permissions requested include:\n" +
                        "• Heart Rate (bpm)\n" +
                        "• Steps (steps)\n" +
                        "• Sleep (hours)\n" +
                        "• Active Calories (cal)\n" +
                        "• Exercise (min)\n" +
                        "• Distance (meters)\n" +
                        "• VO2 Max (mL/kg/min)\n" +
                        "• Weight (kg)\n\n" +
                        "Your data will be handled with the utmost privacy and security, and you retain control over which data is shared. You can modify or revoke these permissions at any time in your device settings.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Checkbox(
                checked = agreementChecked,
                onCheckedChange = { agreementChecked = it }
            )
            Text(
                text = "I have read and agree to the privacy policy and terms of service",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Button(
            onClick = {
                if (agreementChecked) {
                    permissionLauncher.launch(healthConnectManager.permissions)
                }
            },
            enabled = agreementChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthConnectSyncScreenPreview() {
    HealthConnectSyncScreen()
}

