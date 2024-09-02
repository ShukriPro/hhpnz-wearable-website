package com.swevnz.hhpnz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.swevnz.hhpnz.data.HealthDataDB
import com.swevnz.hhpnz.data.HealthRepository
import com.swevnz.hhpnz.ui.screens.HealthConnectSyncScreen
import com.swevnz.hhpnz.ui.screens.HomeScreen
import com.swevnz.hhpnz.ui.screens.LoginScreen
import com.swevnz.hhpnz.ui.theme.HHPNZTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // Late-initialized variables for various components used in the activity
    private lateinit var healthConnectChecker: HealthConnectChecker
    private lateinit var healthConnectManager: HealthConnectManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var healthRepository: HealthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display for the activity
        setupHealthConnect() // Sets up Health Connect and initializes necessary components


        // Check if the user is authenticated
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is signed in, display HomeScreen
            setContent { HHPNZTheme { HomeScreen() } }
        } else {
            setContent { LoginScreen()}
        }

    }

    private fun setupHealthConnect() {
        // Initialize the HealthConnectChecker and HealthConnectManager
        healthConnectChecker = HealthConnectChecker(this)
        healthConnectManager = HealthConnectManager(this)

        // Get an instance of the database and initialize the HealthRepository with various DAOs
        val database = HealthDataDB.getDatabase(this)
        healthRepository = HealthRepository(
            healthConnectManager,
            database.activityDao(),
            database.heartRateDao(),
            database.stepsDao(),
            database.sleepDao(),
            database.activeCaloriesBurnedDao(),
            database.weightDao(),
            database.exerciseDao(),
            database.distanceDao(),
            database.vo2MaxDao()
        )

        // Register for activity results related to permission requests
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) { // If all permissions are granted
                lifecycleScope.launch {
                    try {
                        healthRepository.syncHealthData() // Syncs health data if permissions are granted
                        Toast.makeText(this@MainActivity, "Health data synchronized", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error syncing data: ${e.message}") // Logs any errors during data sync
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Permissions Denied", Toast.LENGTH_SHORT).show() // Shows a message if permissions are denied
            }
        }

        // Launch a coroutine to check the availability of Health Connect
        lifecycleScope.launch {
            when (healthConnectChecker.checkHealthConnectAvailability()) {
                HealthConnectStatus.AVAILABLE -> {
                    if (healthConnectManager.hasAnyPermission()) { // If permissions are already granted
                        healthConnectManager.logAllHealthData() // Logs all health data
                        Toast.makeText(this@MainActivity, "Health Data Logged", Toast.LENGTH_SHORT).show()
                    } else {
                        //permissionLauncher.launch(healthConnectManager.permissions.map { it.toString() }.toTypedArray()) // Requests permissions if not granted
                        setContent {
                            HealthConnectSyncScreen()
                        }
                    }
                }
                HealthConnectStatus.INSTALLED -> Toast.makeText(this@MainActivity, "Health Connect app is installed but not available through the SDK", Toast.LENGTH_SHORT).show()
                HealthConnectStatus.INSTALLABLE -> Toast.makeText(this@MainActivity, "Health Connect can be installed", Toast.LENGTH_SHORT).show()
                HealthConnectStatus.UNAVAILABLE -> Toast.makeText(this@MainActivity, "Health Connect is not available on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
