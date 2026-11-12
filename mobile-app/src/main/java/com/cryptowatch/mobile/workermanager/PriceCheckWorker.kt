package com.cryptowatch.mobile.workermanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cryptowatch.mobile.database.AlertHistoryEntity
import com.cryptowatch.mobile.database.CryptoDatabase
import com.cryptowatch.mobile.datalayer.DataLayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

class PriceCheckWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val db by lazy { CryptoDatabase.getDatabase(applicationContext) }
    private val dataLayerManager by lazy { DataLayerManager.getInstance(applicationContext) }

    override suspend fun doWork(): Result {
        return try {
            val cryptos = db.cryptoDao().getAllCryptos()

            if (cryptos.isEmpty()) {
                return Result.success()
            }

            for (crypto in cryptos) {
                val config = db.alertConfigDao().getConfigByCrypto(crypto.cryptoId)

                if (config?.isActive != true) continue

                val variation = 2.5 // Simulated variation or calculated from market
                val isPositive = variation >= 0

                when (config.thresholdType) {
                    "price" -> {
                        if (crypto.lastPriceUsd >= config.thresholdValue) {
                            triggerAlert(crypto.cryptoId, config.cryptoName, crypto.lastPriceUsd, variation, isPositive)
                        }
                    }
                    "percentage" -> {
                        if (config.thresholdValue > 0 && abs(variation) >= config.thresholdValue) {
                            triggerAlert(crypto.cryptoId, config.cryptoName, crypto.lastPriceUsd, variation, isPositive)
                        }
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun triggerAlert(
        cryptoId: String,
        cryptoName: String,
        currentPrice: Double,
        variation: Double,
        isPositive: Boolean
    ) {
        val history = AlertHistoryEntity(
            cryptoId = cryptoId,
            triggeredPrice = currentPrice,
            variationPercent = variation,
            triggeredAt = System.currentTimeMillis()
        )
        db.alertHistoryDao().insertHistory(history)
        db.alertConfigDao().setActive(cryptoId, false)

        // Send notification/message to watch via DataLayer
        withContext(Dispatchers.IO) {
            dataLayerManager.sendAlertToWatch(
                cryptoId = cryptoId,
                cryptoName = cryptoName.ifEmpty { cryptoId.uppercase() },
                currentPrice = currentPrice,
                variationPercent = variation,
                isPositive = isPositive
            )
        }
    }
}
