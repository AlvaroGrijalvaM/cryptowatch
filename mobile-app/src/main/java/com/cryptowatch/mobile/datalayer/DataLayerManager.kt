package com.cryptowatch.mobile.datalayer

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.wear.data.layer.DataClient
import androidx.wear.data.layer.MessageClient
import androidx.wear.data.layer.DataItem
import androidx.wear.data.layer.PutDataMapRequest
import androidx.wear.data.layer.DataMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataLayerManager(
    context: Context
) {
    private val messageClient: MessageClient = DataClient.getMessageClient(context)
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    // Enviar mensaje directo al reloj (para alertas instantáneas)
    suspend fun sendAlertToWatch(
        cryptoId: String,
        cryptoName: String,
        currentPrice: Double,
        variationPercent: Double,
        isPositive: Boolean
    ) {
        val path = "/crypto/alert"
        val message = buildMessage(cryptoId, cryptoName, currentPrice, variationPercent, isPositive)

        executor.submit {
            try {
                val sendTask = messageClient.sendMessage(path, message)
                sendTask.isSuccessful.addOnCompleteListener { task ->
                    if (task.result) {
                        // Message sent successfully
                    }
                }
            } catch (e: Exception) {
                // Log error - device might not be connected
            }
        }.get(30, java.util.concurrent.TimeUnit.SECONDS)
    }

    // Sincronizar configuraciones de alertas al reloj
    suspend fun syncConfigsToWatch(cryptoIds: List<String>) {
        val putRequest = PutDataMapRequest.create("/crypto/config")
        val dataMap = putRequest.dataMap

        cryptoIds.forEachIndexed { index, cryptoId ->
            dataMap.putString(
                "crypto_id_$index",
                cryptoId
            )
        }

        val putDataTask = putRequest.asPutDataRequest()
        executor.submit {
            try {
                val dataClient = DataClient.getDataClient(context)
                val sendTask = dataClient.putDataItem(putDataTask)
                sendTask.isSuccessful.addOnCompleteListener { task ->
                    if (task.result) {
                        // Configurations synced
                    }
                }
            } catch (e: Exception) {
                // Log error
            }
        }.get(30, java.util.concurrent.TimeUnit.SECONDS)
    }

    // Build the message payload
    private fun buildMessage(
        cryptoId: String,
        cryptoName: String,
        currentPrice: Double,
        variationPercent: Double,
        isPositive: Boolean
    ): String {
        val dataMap = DataMap().apply {
            putString("crypto_id", cryptoId)
            putString("crypto_name", cryptoName)
            putDouble("current_price", currentPrice)
            putDouble("variation_percent", variationPercent)
            putBoolean("is_positive", isPositive)
        }
        return dataMap.toByteArray().toString(java.util.Base64.DEFAULT)
    }

    // Verificar conexión con el reloj
    fun isWatchConnected(): Boolean {
        try {
            val dataClient = DataClient.getDataClient(context)
            val connectionSpec = dataClient.connectionSpec
            return connectionSpec == androidx.wear.data.layer.Wearable.DataClient.ConnectionSpec.WEARABLE
        } catch (e: Exception) {
            return false
        }
    }

    // Obtener IDs de criptomonedas sincronizadas del reloj
    suspend fun getSyncedCryptoIds(): List<String> {
        try {
            val dataItemTask = DataClient.getDataClient(context).getDataItem("/crypto/sync_ids")
            return if (dataItemTask.isSuccessful && dataItemTask.result != null) {
                val dataMap = dataItemTask.result.dataMap
                dataMap.getStringArrayList("crypto_ids") ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Notificar al reloj sobre nuevo precio-check programado
    fun notifyWatchOfWorker() {
        val path = "/crypto/worker_status"
        val message = "price_check_scheduled"

        executor.submit {
            try {
                val sendTask = messageClient.sendMessage(path, message.toByteArray())
                sendTask.isSuccessful.addOnCompleteListener { task ->
                    // Receiver on watch can listen for this message
                }
            } catch (e: Exception) {
                // Device not connected or error
            }
        }
    }

    companion object {
        fun getInstance(context: Context): DataLayerManager = DataLayerManager(context.applicationContext)
    }
}