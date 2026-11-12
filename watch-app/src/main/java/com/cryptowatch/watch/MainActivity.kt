package com.cryptowatch.watch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptowatch.watch.compose.screens.*
import com.cryptowatch.watch.database.CryptoWatchDatabase
import com.cryptowatch.watch.util.VibrationPattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoWatchWatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WatchMainApp(context = this)
                }
            }
        }
    }
}

@Composable
fun WatchMainApp(context: android.content.Context) {
    var currentScreen by remember { mutableStateOf("favorites") }
    var selectedCrypto by remember { mutableStateOf<Triple<String, String, Double>?>(null) }
    
    val notifications = remember { mutableStateListOf<WatchNotificationItem>() }

    LaunchedEffect(currentScreen) {
        if (currentScreen == "notifications") {
            withContext(Dispatchers.IO) {
                val db = CryptoWatchDatabase.getDatabase(context)
                val allHistory = db.alertHistoryDao().getRecentAlerts("btc") + 
                                 db.alertHistoryDao().getRecentAlerts("eth") + 
                                 db.alertHistoryDao().getRecentAlerts("sol")
                val items = allHistory.sortedByDescending { it.triggeredAt }.map { h ->
                    WatchNotificationItem(
                        cryptoId = h.cryptoId,
                        cryptoName = h.cryptoId.uppercase(),
                        price = h.triggeredPrice,
                        variation = h.variationPercent,
                        isPositive = h.variationPercent >= 0
                    )
                }
                withContext(Dispatchers.Main) {
                    notifications.clear()
                    notifications.addAll(items)
                }
            }
        }
    }

    when (currentScreen) {
        "favorites" -> FavoritesScreen(
            onCryptoSelected = { id, name ->
                selectedCrypto = Triple(id, name, 65430.50)
                currentScreen = "quick"
            },
            onAlertConfigure = { _ ->
                VibrationPattern.vibratePositive(context)
            },
            onOpenNotifications = {
                currentScreen = "notifications"
            },
            context = context
        )

        "quick" -> {
            selectedCrypto?.let { (id, name, price) ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        QuickPriceScreen(
                            cryptoId = id,
                            cryptoName = name,
                            currentPrice = price,
                            variationPercent = 2.45,
                            onAlertToggle = {
                                VibrationPattern.vibratePositive(context)
                            },
                            context = context
                        )
                    }
                    OutlinedButton(
                        onClick = { currentScreen = "favorites" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .padding(horizontal = 12.dp)
                    ) {
                        Text("Volver")
                    }
                }
            }
        }

        "notifications" -> NotificationsScreen(
            notifications = notifications,
            onBack = { currentScreen = "favorites" }
        )
    }
}

@Composable
fun CryptoWatchWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
