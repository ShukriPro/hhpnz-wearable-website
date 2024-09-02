package com.swevnz.hhpnz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        Activity::class,
        HeartRate::class,
        Steps::class,
        Sleep::class,
        ActiveCaloriesBurned::class,
        Weight::class,
        Exercise::class,
        Distance::class,
        Vo2Max::class
    ],
    version = 4
)
abstract class HealthDataDB : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun heartRateDao(): HeartRateDao
    abstract fun stepsDao(): StepsDao
    abstract fun sleepDao(): SleepDao
    abstract fun activeCaloriesBurnedDao(): ActiveCaloriesBurnedDao
    abstract fun weightDao(): WeightDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun distanceDao(): DistanceDao
    abstract fun vo2MaxDao(): Vo2MaxDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDataDB? = null

        fun getDatabase(context: Context): HealthDataDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDataDB::class.java,
                    "HealthDataDB"
                )
                    .fallbackToDestructiveMigration() // This line resets the database if the schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}