package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterStart
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar criptomoneda por nombre o símbolo") },
            modifier = Modifier.fillMaxWidth(300.dp).padding(16.dp),
            enabled = true
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filtered = cryptoItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
            items(filtered.size) { index ->
                val crypto = filtered[index]
                CryptoCard(
                    cryptoId = crypto.first,
                    cryptoName = crypto.second,
                    onSelect = { onCryptoSelected(crypto.first, crypto.second) }
                )
            }
        }
    }

    Button(
        onClick = onBack,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    ) {
        Text("Volver")
    }
}