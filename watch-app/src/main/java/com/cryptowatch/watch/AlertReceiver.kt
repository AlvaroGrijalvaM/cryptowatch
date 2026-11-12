package com.cryptowatch.watch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class AlertReceiver : BroadcastReceiver() {

    private val TAG = "AlertReceiver"
    private val CHANNEL_ID = "crypto_alerts_channel"

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            "com.cryptowatch.mobile.ALERT_TRIGGERED" -> {
                handleAlertTriggered(context, intent)
            }
            "com.cryptowatch.mobile.WORKER_STATUS" -> {
                handleWorkerStatus(context, intent)
            }
        }
    }

    private fun handleAlertTriggered(context: Context, intent: Intent) {
        val cryptoName = intent.getStringExtra("crypto_name") ?: "Cripto"
        val cryptoSymbol = intent.getStringExtra("crypto_symbol") ?: ""
        val currentPrice = intent.getDoubleExtra("current_price", 0.0)
        val variationPercent = intent.getDoubleExtra("variation_percent", 0.0)
        val isPositive = intent.getBooleanExtra("is_positive", true)

        Log.d(TAG, "Alerta trigger: $cryptoName $cryptoSymbol - $variationPercent% - Precio: $currentPrice")

        // Solicitar vibración diferenciada
        VibrationManager.vibrateAlert(context, isPositive)

        // Mostrar notificación local en el reloj
        showNotification(context, cryptoName, currentPrice, variationPercent, isPositive)
    }

    private fun showNotification(context: Context, cryptoName: String, price: Double, variation: Double, isPositive: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alertas de Criptomonedas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de umbrales de precio alcanzados"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val title = "Alerta: $cryptoName"
        val content = "Precio: $$price (${if (isPositive) "+" else ""}$variation%)"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun handleWorkerStatus(context: Context, intent: Intent) {
        Log.d(TAG, "Worker status received - may trigger UI update")
    }
}
