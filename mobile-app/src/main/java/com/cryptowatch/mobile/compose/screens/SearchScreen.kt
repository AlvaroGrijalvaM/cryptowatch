package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptowatch.mobile.compose.components.CryptoCard

@Composable
fun SearchScreen(
    onCryptoSelected: (cryptoId: String, cryptoName: String) -> Unit,
    onBack: () -> Unit,
    cryptoItems: List<Pair<String, String>> = listOf(
        Pair("btc", "Bitcoin"),
        Pair("eth", "Ethereum"),
        Pair("sol", "Solana")
    )
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
            placeholder = { Text("Buscar criptomoneda") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        val filtered = cryptoItems.filter { it.second.contains(searchQuery, ignoreCase = true) || it.first.contains(searchQuery, ignoreCase = true) }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered.size) { index ->
                val crypto = filtered[index]
                CryptoCard(
                    cryptoId = crypto.first,
                    cryptoName = crypto.second,
                    onSelect = { onCryptoSelected(crypto.first, crypto.second) }
                )
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Volver")
        }
    }
}
