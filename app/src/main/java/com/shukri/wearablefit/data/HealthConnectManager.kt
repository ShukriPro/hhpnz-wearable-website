package com.shukri.wearablefit.data

import android.content.Context
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.shukri.wearablefit.data.model.HeartRateEntity
import com.shukri.wearablefit.data.model.SleepEntity
import com.shukri.wearablefit.data.model.StepEntity
import com.shukri.wearablefit.data.model.WeightEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class HealthConnectManager(private val context: Context) {

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
    private val healthDao by lazy { HealthDatabase.getDatabase(context).healthDao() }

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class)
    )

    suspend fun hasAllPermissions(): Boolean =
        healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    private suspend inline fun <reified T : Record> readRecords(
        timeRangeFilter: TimeRangeFilter
    ): List<T> = withContext(Dispatchers.IO) {
        val request = ReadRecordsRequest(
            recordType = T::class,
            timeRangeFilter = timeRangeFilter
        )
        healthConnectClient.readRecords(request).records
    }

    suspend fun syncHealthData() {
        withContext(Dispatchers.IO) {
            val endTime = Instant.now()
            val startTime = endTime.minus(30, ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(startTime, endTime)

            val stepData = readRecords<StepsRecord>(timeRangeFilter)
            val sleepData = readRecords<SleepSessionRecord>(timeRangeFilter)
            val weightData = readRecords<WeightRecord>(timeRangeFilter)
            val heartRateData = readRecords<HeartRateRecord>(timeRangeFilter)

            // Insert step data
            val stepEntities = stepData.map { record ->
                StepEntity(
                    id = record.metadata.id,
                    date = record.startTime.toEpochMilli(),
                    count = record.count.toLong(),
                    source = record.metadata.dataOrigin.packageName
                )
            }
            healthDao.insertSteps(stepEntities)

            // Insert sleep data
            val sleepEntities = sleepData.map { record ->
                SleepEntity(
                    id = record.metadata.id,
                    startTime = record.startTime.toEpochMilli(),
                    endTime = record.endTime.toEpochMilli(),
                    durationMinutes = ChronoUnit.MINUTES.between(record.startTime, record.endTime),
                    source = record.metadata.dataOrigin.packageName
                )
            }
            healthDao.insertSleep(sleepEntities)

            // Insert weight data
            val weightEntities = weightData.map { record ->
                WeightEntity(
                    id = record.metadata.id,
                    date = record.time.toEpochMilli(),
                    weightKg = record.weight.inKilograms,
                    source = record.metadata.dataOrigin.packageName
                )
            }
            healthDao.insertWeight(weightEntities)

            // Insert heart rate data
            val heartRateEntities = heartRateData.flatMap { record ->
                record.samples.map { sample ->
                    HeartRateEntity(
                        id = "${record.metadata.id}-${sample.time.toEpochMilli()}",
                        date = sample.time.toEpochMilli(),
                        bpm = sample.beatsPerMinute.toFloat(),
                        source = record.metadata.dataOrigin.packageName
                    )
                }
            }
            healthDao.insertHeartRate(heartRateEntities)

            Log.d("HealthConnectManager", "Data sync and insertion completed")
        }
    }

    suspend fun getHealthData(): String {
        return withContext(Dispatchers.IO) {
            val endTime = Instant.now()
            val startTime = endTime.minus(30, ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(startTime, endTime)

            val stepData = readRecords<StepsRecord>(timeRangeFilter)
            val sleepData = readRecords<SleepSessionRecord>(timeRangeFilter)
            val weightData = readRecords<WeightRecord>(timeRangeFilter)
            val heartRateData = readRecords<HeartRateRecord>(timeRangeFilter)

            buildString {
                append("Health data for the last 30 days:\n\n")

                append("Steps:\n")
                stepData.forEach { record ->
                    append("${record.startTime.toEpochMilli()}: ${record.count} steps\n")
                }
                append("Total Steps: ${stepData.sumOf { it.count }}\n\n")

                append("Sleep Sessions:\n")
                sleepData.forEach { record ->
                    val duration = ChronoUnit.MINUTES.between(record.startTime, record.endTime)
                    append("${record.startTime.toEpochMilli()}: $duration minutes\n")
                }
                append("Average Sleep: ${sleepData.map { ChronoUnit.MINUTES.between(it.startTime, it.endTime) }.average()} minutes\n\n")

                append("Weight:\n")
                weightData.forEach { record ->
                    append("${record.time.toEpochMilli()}: ${record.weight.inKilograms} kg\n")
                }
                append("Average Weight: ${weightData.map { it.weight.inKilograms }.average()} kg\n\n")

                append("Heart Rate:\n")
                heartRateData.forEach { record ->
                    val avgBpm = record.samples.map { it.beatsPerMinute }.average()
                    append("${record.startTime.toEpochMilli()}: $avgBpm bpm\n")
                }
                append("Average Heart Rate: ${heartRateData.flatMap { it.samples }.map { it.beatsPerMinute }.average()} bpm")
            }
        }
    }
}