package com.cryptowatch.mobile.workermanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cryptowatch.mobile.database.CryptoDatabase
import kotlinx.coroutines.runBlocking

class PriceCheckWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val db by lazy { CryptoDatabase.getDatabase(context) }

    override suspend fun doWork(): Int {
        return runBlocking {
            try {
                val cryptos = runBlocking {
                    db.cryptoDao().getAllCryptos()
                }

                if (cryptos.isEmpty()) {
                    return@runBlocking Result.success().code
                }

                for (crypto in cryptos) {
                    val config = runBlocking {
                        db.alertConfigDao().getConfigByCrypto(crypto.cryptoId)
                    }

                    if (config?.isActive != true) continue

                    // Verificar umbral de precio
                    when (config.thresholdType) {
                        "price" -> {
                            if (crypto.lastPriceUsd >= config.thresholdValue) {
                                triggerAlert(crypto, crypto.lastPriceUsd, 0.0, true)
                            }
                        }
                        "percentage" -> {
                            // Lógica simplificada: si hay variación significativa
                            if (Math.abs(crypto.lastPriceUsd - config.thresholdValue) / config.thresholdValue >= 0.10) {
                                triggerAlert(crypto, crypto.lastPriceUsd, 5.0, crypto.lastPriceUsd > config.thresholdValue)
                            }
                        }
                    }
                }

                Result.success()
            } catch (e: Exception) {
                androidx.work.Logger.getInstance(PriceCheckWorker::class.java)
                    .error("PriceCheckWorker", "Error", e)
                Result.failure()
                return Result.failure().code
            }
        }
    }

    private fun triggerAlert(
        crypto: com.cryptowatch.mobile.database.CryptoEntity,
        currentPrice: Double,
        variation: Double,
        isPositive: Boolean
    ) {
        val history = com.cryptowatch.mobile.database.AlertHistoryEntity(
            id = 0,
            cryptoId = crypto.cryptoId,
            triggeredPrice = currentPrice,
            variationPercent = variation,
            triggeredAt = System.currentTimeMillis()
        )
        runBlocking {
            db.alertHistoryDao().insertHistory(history)
        }

        // Desactivar alerta después de disparar
        runBlocking {
            db.alertConfigDao().setActive(crypto.cryptoId, false)
        }

        // Enviar alerta al reloj
        // Usamos un intent o data layer - aquí solo registramos
        androidx.work.Logger.getInstance(PriceCheckWorker::class.java)
            .info("PriceCheckWorker", "Alert triggered: ${crypto.cryptoId} = $currentPrice")
    }
}