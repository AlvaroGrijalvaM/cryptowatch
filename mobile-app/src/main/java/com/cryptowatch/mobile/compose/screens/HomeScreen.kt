package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cryptowatch.mobile.compose.components.CryptoCard

data class CryptoDisplayInfo(
    val id: String,
    val name: String,
    val priceUsd: Double,
    val variation24h: Double
)

@Composable
fun HomeScreen(
    cryptoList: List<CryptoDisplayInfo>,
    onCryptoSelected: (cryptoId: String, cryptoName: String, price: Double, variation: Double) -> Unit,
    onAlertConfigure: (cryptoId: String, cryptoName: String, price: Double) -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateAlerts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CryptoWatch",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Precios en tiempo real (USD)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateSearch,
                modifier = Modifier.weight(1f)
            ) {
                Text("🔍 Buscar")
            }

            OutlinedButton(
                onClick = onNavigateAlerts,
                modifier = Modifier.weight(1f)
            ) {
                Text("🔔 Alertas")
            }
        }

        if (cryptoList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cryptoList.size) { index ->
                    val crypto = cryptoList[index]
                    CryptoCard(
                        cryptoId = crypto.id,
                        cryptoName = crypto.name,
                        price = String.format(java.util.Locale.ROOT, "%.2f", crypto.priceUsd),
                        variation = crypto.variation24h,
                        onSelect = {
                            onCryptoSelected(crypto.id, crypto.name, crypto.priceUsd, crypto.variation24h)
                        },
                        onConfigureAlert = {
                            onAlertConfigure(crypto.id, crypto.name, crypto.priceUsd)
                        }
                    )
                }
            }
        }
    }
}
