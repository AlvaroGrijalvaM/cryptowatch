package com.cryptowatch.mobile.database

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cryptowatch.mobile.datalayer.DataLayerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CryptoDatabase.getDatabase(application)
    private val repository = CryptoRepository(db)

    val cryptosFlow: Flow<List<CryptoEntity>> = repository.getAllCryptosFlow()

    private val _recentHistory = MutableStateFlow<List<AlertHistoryEntity>>(emptyList())
    val recentHistory: StateFlow<List<AlertHistoryEntity>> = _recentHistory.asStateFlow()

    init {
        viewModelScope.launch {
            val existing = db.cryptoDao().getAllCryptos()
            if (existing.isEmpty()) {
                val initialList = listOf(
                    CryptoEntity("btc", "Bitcoin", "BTC", 65430.50),
                    CryptoEntity("eth", "Ethereum", "ETH", 3450.25),
                    CryptoEntity("sol", "Solana", "SOL", 145.80),
                    CryptoEntity("ada", "Cardano", "ADA", 0.45),
                    CryptoEntity("dot", "Polkadot", "DOT", 7.20),
                    CryptoEntity("xrp", "Ripple", "XRP", 0.52),
                    CryptoEntity("doge", "Dogecoin", "DOGE", 0.12),
                    CryptoEntity("avax", "Avalanche", "AVAX", 28.60)
                )
                initialList.forEach { db.cryptoDao().insertCrypto(it) }
            }
            loadRecentHistory()
        }
    }

    fun loadRecentHistory() {
        viewModelScope.launch {
            val allHistory = mutableListOf<AlertHistoryEntity>()
            val cryptos = db.cryptoDao().getAllCryptos()
            for (crypto in cryptos) {
                allHistory.addAll(db.alertHistoryDao().getRecentAlerts(crypto.cryptoId))
            }
            _recentHistory.value = allHistory.sortedByDescending { it.triggeredAt }
        }
    }

    fun saveAlertConfig(cryptoId: String, cryptoName: String, thresholdType: String, thresholdValue: Double) {
        viewModelScope.launch {
            val config = AlertConfigEntity(
                cryptoId = cryptoId,
                cryptoName = cryptoName,
                thresholdType = thresholdType,
                thresholdValue = thresholdValue,
                isActive = true
            )
            db.alertConfigDao().insertConfig(config)
            
            // Sync active config to watch
            val activeConfigs = db.alertConfigDao().getAllConfigs().filter { it.isActive }.map { it.cryptoId }
            DataLayerManager.getInstance(getApplication()).syncConfigsToWatch(activeConfigs)
        }
    }

    suspend fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return repository.getAlertConfig(cryptoId)
    }

    fun setAlertActive(cryptoId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.setAlertActive(cryptoId, isActive)
            val activeConfigs = db.alertConfigDao().getAllConfigs().filter { it.isActive }.map { it.cryptoId }
            DataLayerManager.getInstance(getApplication()).syncConfigsToWatch(activeConfigs)
        }
    }

    fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        viewModelScope.launch {
            repository.registerAlert(cryptoId, price, variation)
            loadRecentHistory()
        }
    }

    fun startPriceCheck(context: Context) {
        repository.startPriceCheckWorker(context)
    }

    fun stopPriceCheck(context: Context) {
        repository.stopPriceCheck(context)
    }
}
