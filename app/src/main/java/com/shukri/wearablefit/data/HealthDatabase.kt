package com.shukri.wearablefit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shukri.wearablefit.data.dao.HealthDao
import com.shukri.wearablefit.data.model.HeartRateEntity
import com.shukri.wearablefit.data.model.SleepEntity
import com.shukri.wearablefit.data.model.StepEntity
import com.shukri.wearablefit.data.model.WeightEntity

@Database(entities = [StepEntity::class, SleepEntity::class, WeightEntity::class, HeartRateEntity::class], version = 4)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "hhpnz3"
                )
                    .fallbackToDestructiveMigration() // Recreates the database instead of migrating
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
