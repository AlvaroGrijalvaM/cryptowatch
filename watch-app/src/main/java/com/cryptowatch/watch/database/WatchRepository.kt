package com.cryptowatch.watch.database

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WatchRepository(
    private val cryptoDao: CryptoDao,
    private val alertConfigDao: AlertConfigDao,
    private val alertHistoryDao: AlertHistoryDao,
    private val workManager: WorkManager
) {

    suspend fun insertCrypto(crypto: CryptoEntity) {
        cryptoDao.insertCrypto(crypto)
    }

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

    suspend fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return alertConfigDao.getConfigByCrypto(cryptoId)
    }

    suspend fun setAlertActive(cryptoId: String, isActive: Boolean) {
        alertConfigDao.setActive(cryptoId, isActive)
    }

    suspend fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        val history = AlertHistoryEntity(
            id = 0,
            cryptoId = cryptoId,
            triggeredPrice = price,
            variationPercent = variation,
            triggeredAt = System.currentTimeMillis()
        )
        alertHistoryDao.insertHistory(history)
    }

    suspend fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return alertHistoryDao.getRecentAlerts(cryptoId)
    }

    fun startPriceCheckWorker() {
        val workRequest = PeriodicWorkRequestBuilder<WatchPriceCheckWorker>(15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "WatchPriceCheckWorker",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }

    fun stopPriceCheckWorker() {
        workManager.cancelUniqueWork("WatchPriceCheckWorker")
    }
}
