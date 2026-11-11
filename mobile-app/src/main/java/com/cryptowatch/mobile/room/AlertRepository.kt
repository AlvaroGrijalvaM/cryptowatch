package com.cryptowatch.mobile.room

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors

class AlertRepository(context: android.content.ApplicationContext) {
    private val dao: AlertDao
    private val executors = Executors.newSingleThreadExecutor()

    init {
        val database = AlertRoomDatabase.getDatabase(context)
        dao = database.alertDao()
    }

    companion object {
        @Volatile
        private var INSTANCE: AlertRepository? = null

        fun getInstance(context: android.content.ApplicationContext): AlertRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AlertRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getAllAlerts(): MutableLiveData<List<AlertEntity>> {
        val result = MutableLiveData<List<AlertEntity>>()
        executors.execute {
            val alerts = dao.getAllAlerts()
            result.postValue(alerts)
        }
        return result
    }

    fun insertAlert(cryptoId: String, cryptoName: String, isActive: Boolean) {
        executors.execute {
            val alert = AlertEntity(cryptoId = cryptoId, cryptoName = cryptoName, isActive = isActive)
            dao.insertAlert(alert)
        }
    }

    fun deleteAlert(cryptoId: String) {
        executors.execute {
            dao.deleteAlert(cryptoId)
        }
    }

    fun updateAlertStatus(cryptoId: String, isActive: Boolean) {
        executors.execute {
            dao.updateAlertActiveStatus(cryptoId, isActive)
        }
    }
}