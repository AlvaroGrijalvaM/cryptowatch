package com.cryptowatch.watch.database

import androidx.lifecycle.LiveData
import androidx.work.WorkManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class WatchRepository(
    private val cryptoDao: CryptoDao,
    private val alertConfigDao: AlertConfigDao,
    private val alertHistoryDao: AlertHistoryDao,
    private val workManager: WorkManager
) {

    // Insertar criptomoneda en background
    suspend fun insertCrypto(crypto: CryptoEntity) = Executors.newSingleThreadExecutor().submit {
        cryptoDao.insertCrypto(crypto).get()
    }.get()

    // Configurar alerta
    suspend fun configureAlert(cryptoId: String, thresholdType: String, thresholdValue: Double): AlertConfigEntity {
        val config = AlertConfigEntity(
            id = 0,
            cryptoId = cryptoId,
            thresholdType = thresholdType,
            thresholdValue = thresholdValue,
            isActive = true,
            createdAt = System.currentTimeMillis()
        )
        alertConfigDao.insertConfig(config)
        return config
    }

    // Obtener configuración de alerta
    fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return alertConfigDao.getConfigByCrypto(cryptoId)
    }

    // Activar/desactivar alerta
    suspend fun setAlertActive(cryptoId: String, isActive: Boolean) {
        alertConfigDao.setActive(cryptoId, isActive)
    }

    // Registrar alerta activada
    fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        val history = AlertHistoryEntity(
            id = 0,
            cryptoId = cryptoId,
            triggeredPrice = price,
            variationPercent = variation,
            triggeredAt = System.currentTimeMillis()
        )
        alertHistoryDao.insertHistory(history)
    }

    // Obtener últimas alertas
    fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return alertHistoryDao.getRecentAlerts(cryptoId)
    }

    // Programar verificación periódica de precios
    fun startPriceCheckWorker(cryptoIds: List<String>) {
        val workRequest = workManager.periodicUniqueWork(
            "PriceCheckWorker",
            workManager.getDefaultPeriodicSpec("PriceCheckWorker"),
            15, java.util.concurrent.TimeUnit.MINUTES
        )
        workManager.enqueueUniquePeriodicWork(
            "PriceCheckWorker",
            WorkManager.enqueueExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    // Detener worker
    fun stopPriceCheckWorker(workManager: WorkManager) {
        workManager.cancelUniqueWork("PriceCheckWorker")
    }
}