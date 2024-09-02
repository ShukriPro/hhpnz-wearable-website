package com.swevnz.hhpnz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey val id: String,
    val type: String,
    val date: Long,
    val value: Long,
    val source: String,
)

/**
 * Entity for Heart Rate records in the database.
 */
@Entity(tableName = "heart_rate")
data class HeartRate(
    @PrimaryKey val id: String,
    val bpm: Int, // Beats per minute
    val timestamp: Long, // Time when heart rate was measured, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Steps records in the database.
 */
@Entity(tableName = "steps")
data class Steps(
    @PrimaryKey val id: String,
    val stepCount: Long, // Number of steps taken
    val timestamp: Long, // Time when the steps were counted, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Sleep records in the database.
 */
@Entity(tableName = "sleep")
data class Sleep(
    @PrimaryKey val id: String,
    val sleepStart: Long, // Start time of sleep in milliseconds since epoch
    val sleepEnd: Long, // End time of sleep in milliseconds since epoch
    val sleepQuality: String? = null, // Optional field for sleep quality, e.g., "good", "poor"
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Active Calories Burned records in the database.
 */
@Entity(tableName = "active_calories_burned")
data class ActiveCaloriesBurned(
    @PrimaryKey val id: String,
    val calories: Double, // Number of active calories burned
    val timestamp: Long, // Time when calories were calculated, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Weight records in the database.
 */
@Entity(tableName = "weight")
data class Weight(
    @PrimaryKey val id: String,
    val weightKg: Double, // Weight in kilograms
    val timestamp: Long, // Time when weight was recorded, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Exercise records in the database.
 */
@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey val id: String,
    val type: String, // Type of exercise, e.g., "running", "swimming"
    val duration: Long, // Duration of the exercise in milliseconds
    val timestamp: Long, // Time when the exercise was started, in milliseconds since epoch
    val caloriesBurned: String, // Calories burned during the exercise
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for Distance records in the database.
 */
@Entity(tableName = "distance")
data class Distance(
    @PrimaryKey val id: String,
    val distanceMeters: Double, // Distance traveled in meters
    val timestamp: Long, // Time when the distance was measured, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)

/**
 * Entity for VO2 Max records in the database.
 */
@Entity(tableName = "vo2_max")
data class Vo2Max(
    @PrimaryKey val id: String,
    val vo2Max: Double, // VO2 Max value
    val timestamp: Long, // Time when VO2 Max was measured, in milliseconds since epoch
    val date: Long // Date when the record was created, in milliseconds since epoch
)