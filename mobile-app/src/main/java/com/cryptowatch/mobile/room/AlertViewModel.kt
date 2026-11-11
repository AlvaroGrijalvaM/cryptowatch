package com.cryptowatch.mobile.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mutableStateFlow
import kotlinx.coroutines.launch

class AlertViewModel(application: android.app.Application) : ViewModel() {
    private val repository = AlertRepository(application)
    private val _alertConfigs = mutableStateFlow<List<AlertEntity>>(listOf())
    val alertConfigs: StateFlow<List<AlertEntity>> = _alertConfigs.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllAlerts().collect { alerts ->
                _alertConfigs.value = alerts
            }
        }
    }
}