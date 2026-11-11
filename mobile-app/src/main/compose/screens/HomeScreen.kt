package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        horizontalAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "CryptoWatch",
            style = MaterialTheme.typography.h2
        )

        val cryptoItems = listOf(
            Pair("btc", "Bitcoin"),
            Pair("eth", "Ethereum"),
            Pair("sol", "Solana")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cryptoItems.size) { index ->            val (cryptoId, cryptoName) = cryptoItems[index]
                CryptoCard(
                    cryptoId = cryptoId,
                    cryptoName = cryptoName,
                    onSelect = { onCryptoSelected(cryptoId, cryptoName) }
                )
            }
        }
    }
}