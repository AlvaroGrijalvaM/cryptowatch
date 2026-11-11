package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptowatch.mobile.compose.components.CryptoCard

@Composable
fun HomeScreen(
    onCryptoSelected: (cryptoId: String, cryptoName: String) -> Unit,
    onAlertConfigure: (cryptoId: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "CryptoWatch",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val cryptoItems = listOf(
            Pair("btc", "Bitcoin"),
            Pair("eth", "Ethereum"),
            Pair("sol", "Solana")
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cryptoItems.size) { index ->
                val (cryptoId, cryptoName) = cryptoItems[index]
                CryptoCard(
                    cryptoId = cryptoId,
                    cryptoName = cryptoName,
                    onSelect = { onCryptoSelected(cryptoId, cryptoName) },
                    onConfigureAlert = { onAlertConfigure(cryptoId) }
                )
            }
        }
    }
}
