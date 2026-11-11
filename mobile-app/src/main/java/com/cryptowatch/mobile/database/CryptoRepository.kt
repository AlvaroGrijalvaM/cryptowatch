package com.cryptowatch.mobile.database

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cryptowatch.mobile.workermanager.PriceCheckWorker
import java.util.concurrent.TimeUnit

class CryptoRepository(private val db: CryptoDatabase) {

    fun getAllCryptosFlow() = db.cryptoDao().getAllCryptosFlow()

    suspend fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return db.alertConfigDao().getConfigByCrypto(cryptoId)
    }

    suspend fun setAlertActive(cryptoId: String, isActive: Boolean) {
        db.alertConfigDao().setActive(cryptoId, isActive)
    }

    suspend fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        val history = AlertHistoryEntity(
            cryptoId = cryptoId,
            triggeredPrice = price,
            variationPercent = variation
        )
        db.alertHistoryDao().insertHistory(history)
    }

    suspend fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return db.alertHistoryDao().getRecentAlerts(cryptoId)
    }

    fun startPriceCheckWorker(context: Context) {
        val request = PeriodicWorkRequestBuilder<PriceCheckWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "PriceCheckWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun stopPriceCheck(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("PriceCheckWorker")
    }
}
