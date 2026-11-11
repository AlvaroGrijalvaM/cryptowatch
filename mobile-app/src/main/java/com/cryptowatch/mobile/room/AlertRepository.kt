package com.cryptowatch.mobile.room

import android.content.Context
import com.cryptowatch.mobile.database.CryptoDatabase
import java.util.concurrent.Executors

class AlertRepository(context: Context) {
    private val dao = CryptoDatabase.getDatabase(context).alertConfigDao()
    private val executors = Executors.newSingleThreadExecutor()

    companion object {
        @Volatile
        private var INSTANCE: AlertRepository? = null

        fun getInstance(context: Context): AlertRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AlertRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
