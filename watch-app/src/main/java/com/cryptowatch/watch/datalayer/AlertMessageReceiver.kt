package com.cryptowatch.watch.datalayer

import android.content.Context
import android.util.Log

class AlertMessageReceiver {
    fun onMessageReceived(context: Context, path: String, data: ByteArray) {
        Log.d("AlertMessageReceiver", "Message received: $path")
    }
}
