package com.cryptowatch.mobile.database

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlinx.coroutines.viewModelScope

class CryptoViewModel(
    private val repository: CryptoRepository
) : ViewModel() {

    // Estado de criptomonedas
    private val _cryptos = repository.getAllCryptosLiveData()
    val cryptosFlow: Flow<List<CryptoEntity>> = _cryptos.asFlow()

    // Estado de precios en tiempo real
    private val _prices = MutableStateFlow<Map<String, Double>>(mapOf())
    val pricesFlow: Flow<Map<String, Double>> = _prices.asFlow()

    // Estado de alertas
    fun getAlertConfig(cryptoId: String): AlertConfigEntity? {
        return repository.getAlertConfig(cryptoId)
    }

    fun setAlertActive(cryptoId: String, isActive: Boolean) {
        repository.setAlertActive(cryptoId, isActive)
    }

    // Registrar alerta activada
    fun registerAlert(cryptoId: String, price: Double, variation: Double) {
        repository.registerAlert(cryptoId, price, variation)
    }

    // Obtener últimas alertas
    fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity> {
        return repository.getRecentAlerts(cryptoId)
    }

    // Worker de verificaciÃ³n de precios
    fun startPriceCheck(cryptoIds: List<String>, context: Context) {
        repository.startPriceCheckWorker(cryptoIds)
    }

    fun stopPriceCheck(workManager: WorkManager) {
        repository.stopPriceCheck(workManager)
    }

    // Cargar precios iniciales al crear el ViewModel
    init {
        viewModelScope.launch {
            // Cargar los 5 criptomonedas mÃ¡s importantes
            val ids = listOf("btc", "eth", "sol", "bnb", "xrp")
            repository.fetchAndCachePrices(ids).also { prices ->
                _prices.value = prices
            }
        }
    }
}