package com.cryptowatch.mobile.datalayer

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

class DataLayerManager(private val context: Context) {

    private val messageClient: MessageClient = Wearable.getMessageClient(context)
    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun sendAlertToWatch(
        cryptoId: String,
        cryptoName: String,
        currentPrice: Double,
        variationPercent: Double,
        isPositive: Boolean
    ) = withContext(Dispatchers.IO) {
        try {
            val path = "/crypto/alert"
            val message = "$cryptoId|$cryptoName|$currentPrice|$variationPercent|$isPositive"
            val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes)
            for (node in nodes) {
                Tasks.await(messageClient.sendMessage(node.id, path, message.toByteArray(StandardCharsets.UTF_8)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncConfigsToWatch(cryptoIds: List<String>) = withContext(Dispatchers.IO) {
        try {
            val putRequest = PutDataMapRequest.create("/crypto/config")
            val dataMap = putRequest.dataMap
            dataMap.putStringArrayList("crypto_ids", ArrayList(cryptoIds))
            val request = putRequest.asPutDataRequest().setUrgent()
            Tasks.await(dataClient.putDataItem(request))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataLayerManager? = null

        fun getInstance(context: Context): DataLayerManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataLayerManager(context.applicationContext).also { INSTANCE = it }
            }
    }
}
