package com.shukri.wearablefit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.shukri.wearablefit.data.HealthConnectManager
import com.shukri.wearablefit.data.HealthDatabase
import com.shukri.wearablefit.ui.theme.WearableFitTheme
import com.shukri.wearablefit.ui.screens.HomeScreen
import com.shukri.wearablefit.viewmodel.HealthViewModel
import com.shukri.wearablefit.viewmodel.HealthViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectManager: HealthConnectManager
    private lateinit var healthViewModel: HealthViewModel

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val allGranted = permissionsResult.all { it.value }
        if (allGranted) {
            runHealthConnect()
        } else {
            Log.e("MainActivity", "Not all permissions were granted")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize HealthConnectManager
        healthConnectManager = HealthConnectManager(this)

        // Initialize HealthViewModel
        val healthDao = HealthDatabase.getDatabase(this).healthDao()
        val viewModelFactory = HealthViewModelFactory(healthDao)
        healthViewModel = ViewModelProvider(this, viewModelFactory)[HealthViewModel::class.java]

        setContent {
            WearableFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(healthViewModel = healthViewModel)
                }
            }
        }

        runHealthConnect()
    }

    private fun runHealthConnect() {
        lifecycleScope.launch {
            try {
                if (!healthConnectManager.hasAllPermissions()) {
                    permissionLauncher.launch(healthConnectManager.permissions.map { it.toString() }.toTypedArray())
                } else {
                    healthConnectManager.syncHealthData()
                    val healthData = healthConnectManager.getHealthData()
                    Log.d("MainActivity", "Health data: $healthData")
                    // Update ViewModel with new data
                    healthViewModel.refreshData()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error running HealthConnect operations", e)
            }
        }
    }
}