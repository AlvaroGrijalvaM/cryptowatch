package com.cryptowatch.mobile.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cryptowatch.mobile.database.AlertConfigEntity
import com.cryptowatch.mobile.database.CryptoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private val db = CryptoDatabase.getDatabase(application)
    private val _alertConfigs = MutableStateFlow<List<AlertConfigEntity>>(emptyList())
    val alertConfigs: StateFlow<List<AlertConfigEntity>> = _alertConfigs.asStateFlow()

    init {
        viewModelScope.launch {
            db.alertConfigDao().getAllConfigsFlow().collect { configs ->
                _alertConfigs.value = configs
            }
        }
    }

    fun toggleAlert(cryptoId: String, currentActive: Boolean) {
        viewModelScope.launch {
            db.alertConfigDao().setActive(cryptoId, !currentActive)
        }
    }
}
