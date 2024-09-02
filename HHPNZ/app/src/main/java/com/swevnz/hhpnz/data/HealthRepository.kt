package com.swevnz.hhpnz.data

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.time.TimeRangeFilter
import com.swevnz.hhpnz.HealthConnectManager
import com.swevnz.hhpnz.data.dao.ActiveCaloriesBurnedDao
import com.swevnz.hhpnz.data.dao.ActivityDao
import com.swevnz.hhpnz.data.dao.DistanceDao
import com.swevnz.hhpnz.data.dao.ExerciseDao
import com.swevnz.hhpnz.data.dao.HeartRateDao
import com.swevnz.hhpnz.data.dao.SleepDao
import com.swevnz.hhpnz.data.dao.StepsDao
import com.swevnz.hhpnz.data.dao.Vo2MaxDao
import com.swevnz.hhpnz.data.dao.WeightDao
import com.swevnz.hhpnz.data.model.ActiveCaloriesBurned
import com.swevnz.hhpnz.data.model.Activity
import com.swevnz.hhpnz.data.model.Distance
import com.swevnz.hhpnz.data.model.Exercise
import com.swevnz.hhpnz.data.model.HeartRate
import com.swevnz.hhpnz.data.model.Sleep
import com.swevnz.hhpnz.data.model.Steps
import com.swevnz.hhpnz.data.model.Vo2Max
import com.swevnz.hhpnz.data.model.Weight
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.reflect.KClass


class HealthRepository(
    private val healthConnectManager: HealthConnectManager,
    private val activityDao: ActivityDao,
    private val heartRateDao: HeartRateDao,
    private val stepsDao: StepsDao,
    private val sleepDao: SleepDao,
    private val activeCaloriesBurnedDao: ActiveCaloriesBurnedDao,
    private val weightDao: WeightDao,
    private val exerciseDao: ExerciseDao,
    private val distanceDao: DistanceDao,
    private val vo2MaxDao: Vo2MaxDao
) {

    suspend fun syncHealthData() {
        val timeRange = TimeRangeFilter.between(
            Instant.now().minus(7, ChronoUnit.DAYS),
            Instant.now()
        )
        syncData(
            "heart_rate",
            HeartRateRecord::class,
            timeRange
        ) { it.samples.firstOrNull()?.beatsPerMinute?.toLong() ?: 0L }
        syncData("steps", StepsRecord::class, timeRange) { it.count }
        syncData(
            "sleep",
            SleepSessionRecord::class,
            timeRange
        ) { ChronoUnit.MINUTES.between(it.startTime, it.endTime) } // Sleep in minutes
        syncData(
            "active_calories",
            ActiveCaloriesBurnedRecord::class,
            timeRange
        ) { it.energy.inKilocalories.toLong() }
        syncData(
            "exercise",
            ExerciseSessionRecord::class,
            timeRange
        ) { ChronoUnit.MINUTES.between(it.startTime, it.endTime) }
        // Convert distance to kilometers
        syncData(
            "distance",
            DistanceRecord::class,
            timeRange
        ) {
            it.distance.inMeters.toLong()
        }

        syncData(
            "vo2_max",
            Vo2MaxRecord::class,
            timeRange
        ) { it.vo2MillilitersPerMinuteKilogram.toFloat().toLong() }
        syncData("weight", WeightRecord::class, timeRange) {
            it.weight.inKilograms.toFloat().toLong()
        }


        // Sync heart rate
        val heartRateRecords = healthConnectManager.readData(HeartRateRecord::class, timeRange)
        heartRateRecords.getOrNull()?.forEach { record ->
            val heartRate = HeartRate(
                id = record.metadata.id,
                bpm = record.samples.firstOrNull()?.beatsPerMinute?.toInt() ?: 0,
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            heartRateDao.insert(heartRate)
        }

        // Sync steps
        val stepsRecords = healthConnectManager.readData(StepsRecord::class, timeRange)
        stepsRecords.getOrNull()?.forEach { record ->
            val steps = Steps(
                id = record.metadata.id,
                stepCount = record.count,
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            stepsDao.insert(steps)
        }

        // Sync sleep
        val sleepRecords = healthConnectManager.readData(SleepSessionRecord::class, timeRange)
        sleepRecords.getOrNull()?.forEach { record ->
            val sleep = Sleep(
                id = record.metadata.id,
                sleepStart = record.startTime.toEpochMilli(),
                sleepEnd = record.endTime.toEpochMilli(),
                sleepQuality = record.metadata.dataOrigin.packageName, // Assuming this holds quality info
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            sleepDao.insert(sleep)
        }

        // Sync active calories burned
        val activeCaloriesRecords = healthConnectManager.readData(ActiveCaloriesBurnedRecord::class, timeRange)
        activeCaloriesRecords.getOrNull()?.forEach { record ->
            val activeCalories = ActiveCaloriesBurned(
                id = record.metadata.id,
                calories = record.energy.inKilocalories.toDouble(),
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            activeCaloriesBurnedDao.insert(activeCalories)
        }

        // Sync weight
        val weightRecords = healthConnectManager.readData(WeightRecord::class, timeRange)
        weightRecords.getOrNull()?.forEach { record ->
            val weight = Weight(
                id = record.metadata.id,
                weightKg = record.weight.inKilograms.toDouble(),
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            weightDao.insert(weight)
        }

        // Sync exercise
        val exerciseRecords = healthConnectManager.readData(ExerciseSessionRecord::class, timeRange)
        exerciseRecords.getOrNull()?.forEach { record ->
            val exercise = Exercise(
                id = record.metadata.id,
                type = record.exerciseType.toString(),
                timestamp = record.startTime.toEpochMilli(),
                duration = ChronoUnit.MILLIS.between(record.startTime, record.endTime),
                caloriesBurned = record.metadata.dataOrigin.packageName, // Assuming this holds calories burned
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            exerciseDao.insert(exercise)
        }

        // Sync distance
        val distanceRecords = healthConnectManager.readData(DistanceRecord::class, timeRange)
        distanceRecords.getOrNull()?.forEach { record ->
            val distance = Distance(
                id = record.metadata.id,
                distanceMeters = record.distance.inMeters.toDouble(),
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            distanceDao.insert(distance)
        }

        // Sync VO2 max
        val vo2MaxRecords = healthConnectManager.readData(Vo2MaxRecord::class, timeRange)
        vo2MaxRecords.getOrNull()?.forEach { record ->
            val vo2Max = Vo2Max(
                id = record.metadata.id,
                vo2Max = record.vo2MillilitersPerMinuteKilogram.toDouble(),
                timestamp = record.metadata.lastModifiedTime.toEpochMilli(),
                date = Instant.now().toEpochMilli() // Current date in milliseconds
            )
            vo2MaxDao.insert(vo2Max)
        }

    }





    private suspend fun <T : Record> syncData(
        type: String,
        recordType: KClass<T>,
        timeRange: TimeRangeFilter,
        valueExtractor: (T) -> Long
    ) {
        val records = healthConnectManager.readData(recordType, timeRange)
        records.getOrNull()?.forEach { record ->
            val activity = Activity(
                id = record.metadata.id,
                type = type,
                date = record.metadata.lastModifiedTime.toEpochMilli(),
                value = valueExtractor(record),
                source = record.metadata.dataOrigin.packageName
            )
            activityDao.insert(activity)
        }
    }
}



