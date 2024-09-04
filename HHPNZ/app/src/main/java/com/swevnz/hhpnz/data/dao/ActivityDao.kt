package com.swevnz.hhpnz.data.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM heart_rate ORDER BY timestamp DESC")
    fun getAllHeartRates(): LiveData<List<HeartRate>>

    @Query("SELECT * FROM heart_rate ORDER BY timestamp DESC LIMIT 1")
    fun getLatestHeartRate(): LiveData<HeartRate?>
}

@Dao
interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(steps: Steps)

    @Query("SELECT * FROM steps ORDER BY timestamp DESC")
    fun getAllSteps(): LiveData<List<Steps>>

    @Query("SELECT * FROM steps ORDER BY timestamp DESC LIMIT 1")
    fun getLatestSteps(): LiveData<Steps?>
}

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleep: Sleep)

    @Query("SELECT * FROM sleep ORDER BY date DESC")
    fun getAllSleepRecords(): LiveData<List<Sleep>>

    @Query("SELECT * FROM sleep ORDER BY date DESC LIMIT 1")
    fun getLatestSleepRecord(): LiveData<Sleep?>
}

@Dao
interface ActiveCaloriesBurnedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activeCalories: ActiveCaloriesBurned)

    @Query("SELECT * FROM active_calories_burned ORDER BY timestamp DESC")
    fun getAllActiveCaloriesBurned(): LiveData<List<ActiveCaloriesBurned>>

    @Query("SELECT * FROM active_calories_burned ORDER BY timestamp DESC LIMIT 1")
    fun getLatestActiveCaloriesBurned(): LiveData<ActiveCaloriesBurned?>
}

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weight: Weight)

    @Query("SELECT * FROM weight ORDER BY timestamp DESC")
    fun getAllWeights(): LiveData<List<Weight>>

    @Query("SELECT * FROM weight ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWeight(): LiveData<Weight?>
}

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercise ORDER BY timestamp DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise ORDER BY timestamp DESC LIMIT 1")
    fun getLatestExercise(): LiveData<Exercise?>
}

@Dao
interface DistanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(distance: Distance)

    @Query("SELECT * FROM distance ORDER BY timestamp DESC")
    fun getAllDistances(): LiveData<List<Distance>>

    @Query("SELECT * FROM distance ORDER BY timestamp DESC LIMIT 1")
    fun getLatestDistance(): LiveData<Distance?>
}

@Dao
interface Vo2MaxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vo2Max: Vo2Max)

    @Query("SELECT * FROM vo2_max ORDER BY timestamp DESC")
    fun getAllVo2MaxRecords(): LiveData<List<Vo2Max>>

    @Query("SELECT * FROM vo2_max ORDER BY timestamp DESC LIMIT 1")
    fun getLatestVo2Max(): LiveData<Vo2Max?>
}
