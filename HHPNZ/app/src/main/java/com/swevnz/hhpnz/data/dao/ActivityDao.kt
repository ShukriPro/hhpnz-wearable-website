package com.swevnz.hhpnz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swevnz.hhpnz.data.model.ActiveCaloriesBurned
import com.swevnz.hhpnz.data.model.Activity
import com.swevnz.hhpnz.data.model.Distance
import com.swevnz.hhpnz.data.model.Exercise
import com.swevnz.hhpnz.data.model.HeartRate
import com.swevnz.hhpnz.data.model.Sleep
import com.swevnz.hhpnz.data.model.Steps
import com.swevnz.hhpnz.data.model.Vo2Max
import com.swevnz.hhpnz.data.model.Weight
import com.swevnz.hhpnz.data.model.*

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity)

    @Query("SELECT * FROM activity WHERE type = :activityType ORDER BY date DESC")
    suspend fun getActivitiesByType(activityType: String): List<Activity>

    // Query to fetch the latest activity for each type
    @Query("""
        SELECT * FROM activity
        WHERE (type, date) IN (
            SELECT type, MAX(date)
            FROM activity
            GROUP BY type
        );
    """)
    suspend fun getLatestActivitiesForAllTypes(): List<Activity>
}

@Dao
interface HeartRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(heartRate: HeartRate)

    @Query("SELECT * FROM heart_rate")
    suspend fun getAllHeartRates(): List<HeartRate>

    @Query("SELECT * FROM heart_rate ORDER BY date DESC, timestamp DESC LIMIT 1")
    suspend fun getLatestHeartRate(): HeartRate?

    @Query("SELECT * FROM heart_rate WHERE date = :date ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestHeartRateForDate(date: Long): HeartRate?
}

@Dao
interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(steps: Steps)

    @Query("SELECT * FROM steps")
    suspend fun getAllSteps(): List<Steps>
}

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleep: Sleep)

    @Query("SELECT * FROM sleep")
    suspend fun getAllSleepRecords(): List<Sleep>
}

@Dao
interface ActiveCaloriesBurnedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activeCalories: ActiveCaloriesBurned)

    @Query("SELECT * FROM active_calories_burned")
    suspend fun getAllActiveCaloriesBurned(): List<ActiveCaloriesBurned>
}

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weight: Weight)

    @Query("SELECT * FROM weight")
    suspend fun getAllWeights(): List<Weight>
}

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercises(): List<Exercise>
}

@Dao
interface DistanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(distance: Distance)

    @Query("SELECT * FROM distance")
    suspend fun getAllDistances(): List<Distance>
}

@Dao
interface Vo2MaxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vo2Max: Vo2Max)

    @Query("SELECT * FROM vo2_max")
    suspend fun getAllVo2MaxRecords(): List<Vo2Max>
}
