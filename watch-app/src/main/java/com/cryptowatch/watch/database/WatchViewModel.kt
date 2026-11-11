package com.cryptowatch.watch.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WatchViewModel(
    private val repository: WatchRepository
) : ViewModel() {

    suspend fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return repository.getAlertConfig(cryptoId)
    }

    fun setAlertActive(cryptoId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.setAlertActive(cryptoId, isActive)
        }
    }

    fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        viewModelScope.launch {
            repository.registerAlert(cryptoId, price, variation)
        }
    }

    suspend fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return repository.getRecentAlerts(cryptoId)
    }

    fun startPriceCheck() {
        repository.startPriceCheckWorker()
    }

    fun stopPriceCheck() {
        repository.stopPriceCheckWorker()
    }
}
