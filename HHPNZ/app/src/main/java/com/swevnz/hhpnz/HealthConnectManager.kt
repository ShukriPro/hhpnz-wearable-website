package com.swevnz.hhpnz

import android.content.Context
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.reflect.KClass

class HealthConnectManager(private val context: Context){

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    // Permission set
    val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(Vo2MaxRecord::class),
    )

    // Check if all permissions are granted
    suspend fun hasAllPermissions(): Boolean =
        healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)

    // Check if any permissions are granted
    suspend fun hasAnyPermission(): Boolean =
        healthConnectClient.permissionController.getGrantedPermissions().isNotEmpty()

    // Request permissions contract
    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    // Read data of a specific record type
    suspend fun <T : Record> readData(
        recordType: KClass<T>,
        timeRange: TimeRangeFilter
    ): Result<List<T>> = withContext(Dispatchers.IO) {
        try {
            val request = ReadRecordsRequest(recordType, timeRange)
            Result.success(healthConnectClient.readRecords(request).records)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Convenience methods for reading specific data types
    suspend fun readStepData(timeRange: TimeRangeFilter) = readData(StepsRecord::class, timeRange)
    suspend fun readHeartRateData(timeRange: TimeRangeFilter) = readData(HeartRateRecord::class, timeRange)
    suspend fun readSleepData(timeRange: TimeRangeFilter) = readData(SleepSessionRecord::class, timeRange)
    suspend fun readWeightData(timeRange: TimeRangeFilter) = readData(WeightRecord::class, timeRange)
    suspend fun readExercise(timeRange: TimeRangeFilter) = readData(ExerciseSessionRecord::class, timeRange)
    suspend fun readDistance(timeRange: TimeRangeFilter) = readData(DistanceRecord::class, timeRange)
    suspend fun readVo2Max(timeRange: TimeRangeFilter) = readData(Vo2MaxRecord::class, timeRange)
    suspend fun readActiveCaloriesBurned(timeRange: TimeRangeFilter) = readData(ActiveCaloriesBurnedRecord::class, timeRange)

    // Log all health data
    suspend fun logAllHealthData() {
        val timeRange = TimeRangeFilter.between(
            Instant.now().minus(7, ChronoUnit.DAYS),
            Instant.now()
        )

        Log.d("HealthConnect", "Heart Rate: ${readHeartRateData(timeRange)}")
        Log.d("HealthConnect", "Steps: ${readStepData(timeRange)}")
        Log.d("HealthConnect", "Sleep: ${readSleepData(timeRange)}")
        Log.d("HealthConnect", "Active Calories Burned: ${readActiveCaloriesBurned(timeRange)}")
        Log.d("HealthConnect", "Exercise: ${readExercise(timeRange)}")
        Log.d("HealthConnect", "Distance: ${readDistance(timeRange)}")
        Log.d("HealthConnect", "VO2 Max: ${readVo2Max(timeRange)}")
        Log.d("HealthConnect", "Weight: ${readWeightData(timeRange)}")
    }

    // Function to handle permission setup
    suspend fun setupPermissions(
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        if (hasAnyPermission()) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }
}
