package com.cryptowatch.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cryptowatch.mobile.compose.components.ConfigureAlertDialog
import com.cryptowatch.mobile.compose.components.CryptoDetailDialog
import com.cryptowatch.mobile.compose.screens.*
import com.cryptowatch.mobile.database.CryptoViewModel

class MainActivity : ComponentActivity() {

    private val cryptoViewModel: CryptoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Start background worker for price check
        cryptoViewModel.startPriceCheck(this)

        setContent {
            CryptoWatchTheme {
                MainAppScreen(cryptoViewModel = cryptoViewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(cryptoViewModel: CryptoViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    
    // State for dialogs
    var selectedCryptoForDetail by remember { mutableStateOf<CryptoDisplayInfo?>(null) }
    var selectedCryptoForAlert by remember { mutableStateOf<Triple<String, String, Double>?>(null) }

    val cryptosEntities by cryptoViewModel.cryptosFlow.collectAsState(initial = emptyList())
    val recentHistory by cryptoViewModel.recentHistory.collectAsState()

    // Map entities to display model with mock variation for display
    val displayList = remember(cryptosEntities) {
        val variations = mapOf(
            "btc" to 2.45,
            "eth" to -1.12,
            "sol" to 5.80,
            "ada" to -0.35,
            "dot" to 1.15,
            "xrp" to 0.88,
            "doge" to -3.20,
            "avax" to 4.10
        )
        cryptosEntities.map { entity ->
            CryptoDisplayInfo(
                id = entity.cryptoId,
                name = entity.name,
                priceUsd = entity.lastPriceUsd,
                variation24h = variations[entity.cryptoId] ?: 0.0
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen == "home",
                        onClick = { currentScreen = "home" },
                        icon = { Text("🏠") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == "search",
                        onClick = { currentScreen = "search" },
                        icon = { Text("🔍") },
                        label = { Text("Buscar") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == "alerts",
                        onClick = { currentScreen = "alerts" },
                        icon = { Text("🔔") },
                        label = { Text("Alertas") }
                    )
                }
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {
                    "home" -> HomeScreen(
                        cryptoList = displayList,
                        onCryptoSelected = { id, name, price, variation ->
                            selectedCryptoForDetail = CryptoDisplayInfo(id, name, price, variation)
                        },
                        onAlertConfigure = { id, name, price ->
                            selectedCryptoForAlert = Triple(id, name, price)
                        },
                        onNavigateSearch = { currentScreen = "search" },
                        onNavigateAlerts = { currentScreen = "alerts" }
                    )

                    "search" -> SearchScreen(
                        cryptoList = displayList,
                        onCryptoSelected = { id, name, price, variation ->
                            selectedCryptoForDetail = CryptoDisplayInfo(id, name, price, variation)
                        },
                        onAlertConfigure = { id, name, price ->
                            selectedCryptoForAlert = Triple(id, name, price)
                        },
                        onBack = { currentScreen = "home" }
                    )

                    "alerts" -> AlertsScreen(
                        onBack = { currentScreen = "home" },
                        onAlertToggled = { id, isActive ->
                            cryptoViewModel.setAlertActive(id, isActive)
                        },
                        recentHistory = recentHistory
                    )
                }
            }
        }

        // Crypto Detail Dialog
        selectedCryptoForDetail?.let { crypto ->
            CryptoDetailDialog(
                cryptoId = crypto.id,
                cryptoName = crypto.name,
                currentPrice = crypto.priceUsd,
                variation24h = crypto.variation24h,
                onDismiss = { selectedCryptoForDetail = null },
                onConfigureAlert = {
                    selectedCryptoForAlert = Triple(crypto.id, crypto.name, crypto.priceUsd)
                }
            )
        }

        // Configure Alert Dialog
        selectedCryptoForAlert?.let { (id, name, price) ->
            ConfigureAlertDialog(
                cryptoId = id,
                cryptoName = name,
                currentPrice = price,
                onDismiss = { selectedCryptoForAlert = null },
                onSaveAlert = { type, value ->
                    cryptoViewModel.saveAlertConfig(id, name, type, value)
                }
            )
        }
    }
}

@Composable
fun CryptoWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
