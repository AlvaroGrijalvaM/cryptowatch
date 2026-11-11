package com.cryptowatch.mobile.workermanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cryptowatch.mobile.database.AlertHistoryEntity
import com.cryptowatch.mobile.database.CryptoDatabase
import kotlin.math.abs

class PriceCheckWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val db by lazy { CryptoDatabase.getDatabase(applicationContext) }

    override suspend fun doWork(): Result {
        return try {
            val cryptos = db.cryptoDao().getAllCryptos()

            if (cryptos.isEmpty()) {
                return Result.success()
            }

            for (crypto in cryptos) {
                val config = db.alertConfigDao().getConfigByCrypto(crypto.cryptoId)

                if (config?.isActive != true) continue

                when (config.thresholdType) {
                    "price" -> {
                        if (crypto.lastPriceUsd >= config.thresholdValue) {
                            triggerAlert(crypto.cryptoId, crypto.lastPriceUsd, 0.0)
                        }
                    }
                    "percentage" -> {
                        if (config.thresholdValue > 0 && abs(crypto.lastPriceUsd - config.thresholdValue) / config.thresholdValue >= 0.10) {
                            triggerAlert(crypto.cryptoId, crypto.lastPriceUsd, 5.0)
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
        currentPrice: Double,
        variation: Double
    ) {
        val history = AlertHistoryEntity(
            cryptoId = cryptoId,
            triggeredPrice = currentPrice,
            variationPercent = variation,
            triggeredAt = System.currentTimeMillis()
        )
        db.alertHistoryDao().insertHistory(history)
        db.alertConfigDao().setActive(cryptoId, false)
    }
}
