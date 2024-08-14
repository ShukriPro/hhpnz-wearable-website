package com.shukri.wearablefit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shukri.wearablefit.data.dao.HealthDao
import com.shukri.wearablefit.data.model.HeartRateEntity
import com.shukri.wearablefit.data.model.SleepEntity
import com.shukri.wearablefit.data.model.StepEntity
import com.shukri.wearablefit.data.model.WeightEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HealthViewModel(private val healthDao: HealthDao) : ViewModel() {

    private val _latestStep = MutableStateFlow<StepEntity?>(null)
    val latestStep: StateFlow<StepEntity?> = _latestStep

    private val _latestSleep = MutableStateFlow<SleepEntity?>(null)
    val latestSleep: StateFlow<SleepEntity?> = _latestSleep

    private val _latestWeight = MutableStateFlow<WeightEntity?>(null)
    val latestWeight: StateFlow<WeightEntity?> = _latestWeight

    private val _latestHeartRate = MutableStateFlow<HeartRateEntity?>(null)
    val latestHeartRate: StateFlow<HeartRateEntity?> = _latestHeartRate

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _latestStep.value = healthDao.getLatestStep()
            _latestSleep.value = healthDao.getLatestSleep()
            _latestWeight.value = healthDao.getLatestWeight()
            _latestHeartRate.value = healthDao.getLatestHeartRate()
        }
    }
}

class HealthViewModelFactory(private val healthDao: HealthDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthViewModel(healthDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


