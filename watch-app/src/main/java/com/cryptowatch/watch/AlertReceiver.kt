package com.cryptowatch.watch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cryptowatch.watch.VibrationManager

class AlertReceiver : BroadcastReceiver() {

    private val TAG = "AlertReceiver"

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
        val cryptoName = intent.getStringExtra("crypto_name")
        val cryptoSymbol = intent.getStringExtra("crypto_symbol")
        val currentPrice = intent.getDoubleExtra("current_price", 0.0)
        val variationPercent = intent.getDoubleExtra("variation_percent", 0.0)
        val isPositive = intent.getBooleanExtra("is_positive", true)

        Log.d(TAG, "Alerta trigger: $cryptoName $cryptoSymbol - $variationPercent% - Precio: $currentPrice")

        // Solicitar vibración diferenciada
        VibrationManager.vibrateAlert(context, isPositive)

        // TODO: Mostrar notificación en el reloj con los detalles
        // La UI puede observar cambios en ViewModel para mostrar la alerta
    }

    private fun handleWorkerStatus(context: Context, intent: Intent) {
        Log.d(TAG, "Worker status received - may trigger UI update")
        // El worker PriceCheck se ejecutó - la UI puede refrescarse si es necesario
        // Por ejemplo, podría setear un LiveData que la Activity observa
    }
}