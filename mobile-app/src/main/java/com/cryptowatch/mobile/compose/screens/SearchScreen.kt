package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptowatch.mobile.compose.components.CryptoCard
import java.util.Locale

@Composable
fun SearchScreen(
    cryptoList: List<CryptoDisplayInfo>,
    onCryptoSelected: (cryptoId: String, cryptoName: String, price: Double, variation: Double) -> Unit,
    onAlertConfigure: (cryptoId: String, cryptoName: String, price: Double) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar por nombre o símbolo (BTC, ETH...)") },
            placeholder = { Text("Ej. Bitcoin o sol") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        val filtered = remember(searchQuery, cryptoList) {
            if (searchQuery.isBlank()) {
                cryptoList
            } else {
                cryptoList.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.id.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron resultados para '$searchQuery'",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered.size) { index ->
                    val crypto = filtered[index]
                    CryptoCard(
                        cryptoId = crypto.id,
                        cryptoName = crypto.name,
                        price = String.format(Locale.ROOT, "%.2f", crypto.priceUsd),
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

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Volver al Inicio")
        }
    }
}
