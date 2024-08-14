package com.shukri.wearablefit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: Long,  // Changed to Long
    @ColumnInfo(name = "count") val count: Long,
    @ColumnInfo(name = "source") val source: String
)

@Entity(tableName = "sleep")
data class SleepEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "start_time") val startTime: Long,  // Changed to Long
    @ColumnInfo(name = "end_time") val endTime: Long,  // Changed to Long
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Long,
    @ColumnInfo(name = "source") val source: String
)

@Entity(tableName = "weight")
data class WeightEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: Long,  // Changed to Long
    @ColumnInfo(name = "weight_kg") val weightKg: Double,
    @ColumnInfo(name = "source") val source: String
)

@Entity(tableName = "heart_rate")
data class HeartRateEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: Long,  // Changed to Long
    @ColumnInfo(name = "bpm") val bpm: Float,
    @ColumnInfo(name = "source") val source: String
)