package com.swevnz.hhpnz

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * HealthConnectChecker class
 * This class is responsible for checking the availability and status of Health Connect on the device.
 * It also provides access to the HealthConnectClient for interacting with Health Connect.
 * @param context The application context, used for accessing system services and resources.
 */
class HealthConnectChecker(private val context: Context) {

    // Lazy initialization of HealthConnectClient
    private val healthConnectClientInstance by lazy { HealthConnectClient.getOrCreate(context) }

    /**
     * Checks the availability status of Health Connect.
     * This method performs a series of checks to determine if Health Connect is supported,
     * installed, and accessible on the device.
     *
     * @return HealthConnectStatus indicating the current state of Health Connect
     */
    suspend fun checkHealthConnectAvailability(): HealthConnectStatus = withContext(Dispatchers.Default) {
        when {
            isHealthConnectAvailable() -> HealthConnectStatus.AVAILABLE
            isHealthConnectInstalled() -> HealthConnectStatus.INSTALLED
            isHealthConnectInstallable() -> HealthConnectStatus.INSTALLABLE
            else -> HealthConnectStatus.UNAVAILABLE
        }
    }

    /**
     * Checks if Health Connect is available through the SDK.
     * This is the preferred way to access Health Connect on Android 14 and above.
     *
     * @return true if Health Connect is available through the SDK, false otherwise
     */
    private fun isHealthConnectAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }
    /**
     * Checks if the Health Connect app is installed on the device.
     * This is relevant for devices running Android 13 or lower, where
     * Health Connect might be available as a separate app.
     *
     * @return true if Health Connect app is installed, false otherwise
     */
    private fun isHealthConnectInstalled(): Boolean {
        //val intent = Intent("androidx.health.ACTION_HEALTH_CONNECT_SHOW_SETTINGS")
        //return context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null

        val healthConnectPackageName = "com.google.android.apps.healthdata"
        val packageManager = context.packageManager

        return try {
            // Try to get package info. If successful, the package is installed.
            packageManager.getPackageInfo(healthConnectPackageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException)
        {
            // Package not found, try alternative methods

            // Check if the Health Connect settings activity can be resolved
            val settingsIntent = Intent("androidx.health.ACTION_HEALTH_CONNECT_SHOW_SETTINGS")
            val settingsResolvable = settingsIntent.resolveActivity(packageManager) != null

            // Check if the Health Connect permissions activity can be resolved
            val permissionsIntent = Intent("androidx.health.ACTION_HEALTH_PERMISSIONS")
            val permissionsResolvable = permissionsIntent.resolveActivity(packageManager) != null

            // Return true if either intent can be resolved
            settingsResolvable || permissionsResolvable
        }
    }

    /**
     * Checks if Health Connect can be installed on the device.
     * This is relevant for devices that support Health Connect but don't have it pre-installed.
     *
     * @return true if Health Connect can be installed, false otherwise
     */
    private fun isHealthConnectInstallable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.android.vending")
            data = android.net.Uri.parse("market://details?id=com.google.android.apps.healthdata")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
    }

    /**
     * Creates an intent to open Health Connect settings or install it.
     *
     * @return An Intent to open Health Connect settings or install it, or null if not possible
     */
    suspend fun getHealthConnectIntent(): Intent? = withContext(Dispatchers.Default) {
        return@withContext when {
            isHealthConnectAvailable() -> {
                // Use PermissionController to create the intent for Health Connect settings
                PermissionController.createRequestPermissionResultContract().createIntent(
                    context,
                    setOf() // Empty set as we're just opening settings, not requesting specific permissions
                )
            }
            isHealthConnectInstalled() -> {
                // Open Health Connect app directly
                context.packageManager.getLaunchIntentForPackage("com.google.android.apps.healthdata")
                    ?: Intent("androidx.health.ACTION_HEALTH_CONNECT_SHOW_SETTINGS")
            }
            isHealthConnectInstallable() -> {
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = android.net.Uri.parse("market://details?id=com.google.android.apps.healthdata")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            else -> null
        }
    }

    /**
     * Provides access to the HealthConnectClient.
     * This client can be used to interact with Health Connect, such as reading or writing health data.
     *
     * @return The HealthConnectClient instance
     */

    fun getHealthConnectClient(): HealthConnectClient {
        if (!isHealthConnectAvailable()) {
            throw IllegalStateException("Health Connect is not available")
        }
        return healthConnectClientInstance
    }
}

/**
 * Enum representing the different states of Health Connect availability
 */
enum class HealthConnectStatus {
    AVAILABLE,    // Health Connect is available through the SDK
    INSTALLED,    // Health Connect app is installed
    INSTALLABLE,  // Health Connect can be installed
    UNAVAILABLE   // Health Connect is not supported on this device
}