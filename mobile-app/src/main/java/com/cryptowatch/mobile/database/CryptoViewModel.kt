package com.cryptowatch.mobile.database

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CryptoRepository(CryptoDatabase.getDatabase(application))

    val cryptosFlow: Flow<List<CryptoEntity>> = repository.getAllCryptosFlow()

    private val _prices = MutableStateFlow<Map<String, Double>>(mapOf())
    val pricesFlow: StateFlow<Map<String, Double>> = _prices.asStateFlow()

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

    fun startPriceCheck(context: Context) {
        repository.startPriceCheckWorker(context)
    }

    fun stopPriceCheck(context: Context) {
        repository.stopPriceCheck(context)
    }
}
