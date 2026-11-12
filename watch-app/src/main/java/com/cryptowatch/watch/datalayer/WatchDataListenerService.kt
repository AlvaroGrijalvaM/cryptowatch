package com.cryptowatch.watch.datalayer

import android.content.Intent
import android.util.Log
import com.cryptowatch.watch.database.AlertConfigEntity
import com.cryptowatch.watch.database.AlertHistoryEntity
import com.cryptowatch.watch.database.CryptoWatchDatabase
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class WatchDataListenerService : WearableListenerService() {

    private val tag = "WatchDataListener"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d(tag, "Message received path: ${messageEvent.path}")

        if (messageEvent.path == "/crypto/alert") {
            val dataStr = String(messageEvent.data, StandardCharsets.UTF_8)
            Log.d(tag, "Alert data payload: $dataStr")

            // Format: cryptoId|cryptoName|currentPrice|variationPercent|isPositive
            val parts = dataStr.split("|")
            if (parts.size >= 5) {
                val cryptoId = parts[0]
                val cryptoName = parts[1]
                val currentPrice = parts[2].toDoubleOrNull() ?: 0.0
                val variationPercent = parts[3].toDoubleOrNull() ?: 0.0
                val isPositive = parts[4].toBooleanStrictOrNull() ?: true

                // 1. Broadcast to AlertReceiver for vibration & system notification
                val intent = Intent("com.cryptowatch.mobile.ALERT_TRIGGERED").apply {
                    putExtra("crypto_id", cryptoId)
                    putExtra("crypto_name", cryptoName)
                    putExtra("crypto_symbol", cryptoId.uppercase())
                    putExtra("current_price", currentPrice)
                    putExtra("variation_percent", variationPercent)
                    putExtra("is_positive", isPositive)
                    setPackage(packageName)
                }
                sendBroadcast(intent)

                // 2. Persist in Wear OS local database
                scope.launch {
                    val db = CryptoWatchDatabase.getDatabase(applicationContext)
                    val history = AlertHistoryEntity(
                        cryptoId = cryptoId,
                        triggeredPrice = currentPrice,
                        variationPercent = variationPercent,
                        triggeredAt = System.currentTimeMillis(),
                    )
                    db.alertHistoryDao().insertHistory(history)
                }
            }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path == "/crypto/config") {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val cryptoIds = dataMap.getStringArrayList("crypto_ids") ?: ArrayList()
                    Log.d(tag, "Synced active crypto configs from phone: $cryptoIds")

                    scope.launch {
                        val db = CryptoWatchDatabase.getDatabase(applicationContext)
                        for (cryptoId in cryptoIds) {
                            val existing = db.alertConfigDao().getConfigByCrypto(cryptoId)
                            if (existing == null) {
                                db.alertConfigDao().insertConfig(
                                    AlertConfigEntity(
                                        cryptoId = cryptoId,
                                        thresholdType = "price",
                                        thresholdValue = 0.0,
                                        isActive = true,
                                        createdAt = System.currentTimeMillis(),
                                    )
                                )
                            } else {
                                db.alertConfigDao().setActive(cryptoId, isActive = true)
                            }
                        }
                    }
                }
            }
        }
    }
}
