package com.swevnz.hhpnz.data.viewModel

import androidx.lifecycle.ViewModel
import com.swevnz.hhpnz.data.dao.ActiveCaloriesBurnedDao
import com.swevnz.hhpnz.data.dao.DistanceDao
import com.swevnz.hhpnz.data.dao.ExerciseDao
import com.swevnz.hhpnz.data.dao.HeartRateDao
import com.swevnz.hhpnz.data.dao.SleepDao
import com.swevnz.hhpnz.data.dao.StepsDao
import com.swevnz.hhpnz.data.dao.Vo2MaxDao
import com.swevnz.hhpnz.data.dao.WeightDao

class HomeViewModel(
    heartRateDao: HeartRateDao,
    stepsDao: StepsDao,
    sleepDao: SleepDao,
    activeCaloriesBurnedDao: ActiveCaloriesBurnedDao,
    weightDao: WeightDao,
    exerciseDao: ExerciseDao,
    distanceDao: DistanceDao,
    vo2MaxDao: Vo2MaxDao
) : ViewModel() {

    // Heart Rate Data
    val latestHeartRate = heartRateDao.getLatestHeartRate()
    val allHeartRates = heartRateDao.getAllHeartRates()

    // Steps Data
    val latestSteps = stepsDao.getLatestSteps()
    val allSteps = stepsDao.getAllSteps()

    // Sleep Data
    val latestSleepRecord = sleepDao.getLatestSleepRecord()
    val allSleepRecords = sleepDao.getAllSleepRecords()

    // Active Calories Burned Data
    val latestActiveCaloriesBurned = activeCaloriesBurnedDao.getLatestActiveCaloriesBurned()
    val allActiveCaloriesBurned = activeCaloriesBurnedDao.getAllActiveCaloriesBurned()

    // Weight Data
    val latestWeight = weightDao.getLatestWeight()
    val allWeights = weightDao.getAllWeights()

    // Exercise Data
    val latestExercise = exerciseDao.getLatestExercise()
    val allExercises = exerciseDao.getAllExercises()

    // Distance Data
    val latestDistance = distanceDao.getLatestDistance()
    val allDistances = distanceDao.getAllDistances()

    // Vo2Max Data
    val latestVo2Max = vo2MaxDao.getLatestVo2Max()
    val allVo2MaxRecords = vo2MaxDao.getAllVo2MaxRecords()

}