package com.cryptowatch.watch.database

import androidx.lifecycle.ViewModel
import com.cryptowatch.watch.database.CryptoWatchDatabase.AlertConfigEntity

class WatchViewModel(
    private val repository: WatchRepository
) : ViewModel() {

    // Estado de configuración de alertas
    fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return repository.getAlertConfig(cryptoId)
    }

    fun setAlertActive(cryptoId: String, isActive: Boolean) {
        repository.setAlertActive(cryptoId, isActive)
    }

    // Registrar alerta activada
    fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        repository.registerAlert(cryptoId, price, variation)
    }

    // Obtener últimas alertas
    fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return repository.getRecentAlerts(cryptoId)
    }

    // Worker de verificación de precios
    fun startPriceCheck(cryptoIds: List<String>) {
        repository.startPriceCheckWorker(cryptoIds)
    }

    fun stopPriceCheck(workManager: androidx.work.WorkManager) {
        repository.stopPriceCheck(workManager)
    }
}