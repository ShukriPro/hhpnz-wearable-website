package com.swevnz.hhpnz.ui.dialog

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swevnz.hhpnz.ui.screens.HealthConnectSyncScreen

@Composable
fun HealthConnectInstallDialog() {
    // Get the context (required to launch intents)
    val context = LocalContext.current

    // State to track if the user has chosen to proceed to HealthConnectSyncScreen
    var navigateToHealthConnectSync by remember { mutableStateOf(false) }

    // If user chooses to sync, navigate to HealthConnectSyncScreen
    if (navigateToHealthConnectSync) {
        HealthConnectSyncScreen()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp)
                .clip(RoundedCornerShape(28.dp)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Health Connect Required",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HHPNZ requires the Health Connect app to sync and manage your health data. Please install Health Connect from the Play Store to continue using the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        handleHealthConnectInstallation(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                ) {
                    Text("Install & Allow Health Connect", color = Color.White)
                }
            }
        }
    }
}

// Function to handle Health Connect installation or app opening logic
private fun handleHealthConnectInstallation(context: Context) {
    // Check if Health Connect is already installed
    if (isHealthConnectInstalled(context)) {
        // Launch Health Connect if installed
        val intent = context.packageManager.getLaunchIntentForPackage("com.google.android.apps.healthdata")
            ?: Intent("androidx.health.ACTION_HEALTH_CONNECT_SHOW_SETTINGS") // Show Health Connect settings if app can't be launched
        context.startActivity(intent)
    } else if (isHealthConnectInstallable()) {
        // Open the Play Store to install Health Connect if it's installable
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.android.vending") // Target Google Play Store
            data = Uri.parse("market://details?id=com.google.android.apps.healthdata") // Health Connect app's Play Store link
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(installIntent)
    } else {
        // Handle case where Health Connect cannot be installed (e.g., unsupported device)
        showToast(context, "Health Connect is not available on this device.")
    }
}

// Check if Health Connect is already installed on the user's device
fun isHealthConnectInstalled(context: Context): Boolean {
    return try {
        // Try to get package info for Health Connect
        context.packageManager.getPackageInfo("com.google.android.apps.healthdata", 0)
        true // If successful, Health Connect is installed
    } catch (e: PackageManager.NameNotFoundException) {
        false // Health Connect not found
    }
}

// Logic to check if Health Connect can be installed (for now returns true, you can add more checks)
fun isHealthConnectInstallable(): Boolean {
    return true // Assume it's installable, add more logic if needed (e.g., checking Google Play availability)
}

// Helper function to show a toast message
fun showToast(context: Context, message: String) {
    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun HealthConnectInstallDialogPreview() {
    MaterialTheme {
        HealthConnectInstallDialog()
    }
}