package com.swevnz.hhpnz.data.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.swevnz.hhpnz.data.HealthDataDB
import com.swevnz.hhpnz.data.dao.ActiveCaloriesBurnedDao
import com.swevnz.hhpnz.data.dao.ActivityDao
import com.swevnz.hhpnz.data.dao.DistanceDao
import com.swevnz.hhpnz.data.dao.ExerciseDao
import com.swevnz.hhpnz.data.dao.HeartRateDao
import com.swevnz.hhpnz.data.dao.SleepDao
import com.swevnz.hhpnz.data.dao.StepsDao
import com.swevnz.hhpnz.data.dao.Vo2MaxDao
import com.swevnz.hhpnz.data.dao.WeightDao
import com.swevnz.hhpnz.data.model.Activity
import com.swevnz.hhpnz.data.model.HeartRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val activityDao: ActivityDao by lazy { HealthDataDB.getDatabase(application).activityDao() }
    private val heartRateDao: HeartRateDao by lazy { HealthDataDB.getDatabase(application).heartRateDao() }
    private val stepsDao: StepsDao by lazy { HealthDataDB.getDatabase(application).stepsDao() }
    private val sleepDao: SleepDao by lazy { HealthDataDB.getDatabase(application).sleepDao() }
    private val activeCaloriesBurnedDao: ActiveCaloriesBurnedDao by lazy { HealthDataDB.getDatabase(application).activeCaloriesBurnedDao() }
    private val weightDao: WeightDao by lazy { HealthDataDB.getDatabase(application).weightDao() }
    private val exerciseDao: ExerciseDao by lazy { HealthDataDB.getDatabase(application).exerciseDao() }
    private val distanceDao: DistanceDao by lazy { HealthDataDB.getDatabase(application).distanceDao() }
    private val vo2MaxDao: Vo2MaxDao by lazy { HealthDataDB.getDatabase(application).vo2MaxDao() }

    // LiveData to store the latest activities for all types
    private val _latestActivities = MutableLiveData<List<Activity>>()
    val latestActivities: LiveData<List<Activity>> = _latestActivities
    // Function to fetch the latest activities for all types
    fun fetchLatestActivitiesForAllTypes() {
        viewModelScope.launch {
            val latestActivities = activityDao.getLatestActivitiesForAllTypes()
            _latestActivities.value = sortActivities(latestActivities)
            // Add logging to check if the data is fetched correctly
            latestActivities.forEach {
                Log.d(
                    "ActivityViewModel",
                    "Fetched activity: Type=${it.type}, Value=${it.value}, Date=${Date(it.date)}, Source=${it.source}"
                )
            }
             }
    }
    // Ensure all metric types are present and sort them by value (non-zero at top, zero at bottom)
    fun ensureAllMetricsPresent(activities: List<Activity>): List<Activity> {
        val metricTypes = listOf("heart_rate", "steps", "sleep", "active calories", "exercise", "distance", "vo2max", "weight")
        val activityMap = activities.associateBy { it.type }

        val allMetrics = metricTypes.map { type ->
            activityMap[type] ?: Activity(id = "0", type = type, value = 0, date = 0, source = "Default")
        }

        return sortActivities(allMetrics)
    }
    // Helper function to sort activities by value
    private fun sortActivities(activities: List<Activity>): List<Activity> {
        return activities.sortedByDescending { it.date }
    }
    // Function to format the type to a human-readable format
    fun formatType(type: String): String {
        return type.replace('_', ' ').split(" ").joinToString(" ") { it.capitalize() }
    }

    private val _detailedActivities = MutableStateFlow<List<Activity>>(emptyList())
    val detailedActivities: StateFlow<List<Activity>> = _detailedActivities
    fun fetchDetailedActivities(activityType: String) {
        viewModelScope.launch {
            // Replace this with actual data fetching from your database
            _detailedActivities.value = activityDao.getActivitiesByType(activityType)
        }
    }


}
