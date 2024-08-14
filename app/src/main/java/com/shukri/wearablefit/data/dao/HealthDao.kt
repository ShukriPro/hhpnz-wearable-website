package com.shukri.wearablefit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shukri.wearablefit.data.model.HeartRateEntity
import com.shukri.wearablefit.data.model.SleepEntity
import com.shukri.wearablefit.data.model.StepEntity
import com.shukri.wearablefit.data.model.WeightEntity


@Dao
interface HealthDao {

    // Inserts a list of StepEntity objects into the steps table, replacing any conflicting entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    // Inserts a list of SleepEntity objects into the sleep table, replacing any conflicting entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleep(sleep: List<SleepEntity>)

    // Inserts a list of WeightEntity objects into the weight table, replacing any conflicting entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: List<WeightEntity>)

    // Inserts a list of HeartRateEntity objects into the heart_rate table, replacing any conflicting entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeartRate(heartRate: List<HeartRateEntity>)

    // Retrieves the most recent 30 StepEntity objects from the steps table, ordered by date in descending order
    @Query("SELECT * FROM steps ORDER BY date DESC LIMIT 30")
    suspend fun getRecentSteps(): List<StepEntity>

    // Retrieves the most recent 30 SleepEntity objects from the sleep table, ordered by start_time in descending order
    @Query("SELECT * FROM sleep ORDER BY start_time DESC LIMIT 30")
    suspend fun getRecentSleep(): List<SleepEntity>

    // Retrieves the most recent 30 WeightEntity objects from the weight table, ordered by date in descending order
    @Query("SELECT * FROM weight ORDER BY date DESC LIMIT 30")
    suspend fun getRecentWeight(): List<WeightEntity>

    // Retrieves the most recent 30 HeartRateEntity objects from the heart_rate table, ordered by date in descending order
    @Query("SELECT * FROM heart_rate ORDER BY date DESC LIMIT 30")
    suspend fun getRecentHeartRate(): List<HeartRateEntity>

    @Query("SELECT * FROM steps ORDER BY date DESC LIMIT 1")
    suspend fun getLatestStep(): StepEntity?

    @Query("SELECT * FROM weight ORDER BY date DESC LIMIT 1")
    suspend fun getLatestWeight(): WeightEntity?

    @Query("SELECT * FROM heart_rate ORDER BY date DESC LIMIT 1")
    suspend fun getLatestHeartRate(): HeartRateEntity?

    @Query("SELECT * FROM sleep ORDER BY start_time DESC LIMIT 1")
    suspend fun getLatestSleep(): SleepEntity?

    @Query("SELECT * FROM steps WHERE date > :since")
    suspend fun getStepsSince(since: Long): List<StepEntity>

    @Query("SELECT * FROM sleep WHERE start_time > :since")
    suspend fun getSleepSince(since: Long): List<SleepEntity>

    @Query("SELECT * FROM weight WHERE date > :since")
    suspend fun getWeightSince(since: Long): List<WeightEntity>

    @Query("SELECT * FROM heart_rate WHERE date > :since")
    suspend fun getHeartRateSince(since: Long): List<HeartRateEntity>

    @Query("SELECT * FROM steps")
    suspend fun getAllSteps(): List<StepEntity>

    @Query("SELECT * FROM sleep")
    suspend fun getAllSleep(): List<SleepEntity>

    @Query("SELECT * FROM weight")
    suspend fun getAllWeight(): List<WeightEntity>

    @Query("SELECT * FROM heart_rate")
    suspend fun getAllHeartRate(): List<HeartRateEntity>
}
