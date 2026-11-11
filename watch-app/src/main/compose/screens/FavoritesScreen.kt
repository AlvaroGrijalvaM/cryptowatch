package com.cryptowatch.watch.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.TextWeight

@Composable
fun FavoritesScreen(
    onCryptoSelected: (cryptoId: String, cryptoName: String) -> Unit,
    onAlertConfigure: (cryptoId: String) -> Unit,
    context: android.content.Context
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterStart
    ) {
        // Barra de búsqueda pequeña para reloj
        if (showSearch) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar") },
                modifier = Modifier
                    .fillMaxWidth(200.dp)
                    .onTextChange { showSearch = it.isNotEmpty() }
            )
        } else {
            Button(
                onClick = { showSearch = true },
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text("🔍", style = MaterialTheme.typography.h4)
            }
        }

        Text(
            text = "Favoritas",
            style = MaterialTheme.typography.h5
        )

        // Favoritas por defecto - en una implementación real cargarían desde DB o Data Layer
        val favoriteCryptos = listOf(
            Pair("btc", "Bitcoin"),
            Pair("eth", "Ethereum"),
            Pair("sol", "Solana")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(Float.MaxValueFloat),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterStart
        ) {
            items(favoriteCryptos.size) { index ->
                val (cryptoId, cryptoName) = favoriteCryptos[index]
                CryptoCardWatch(
                    cryptoId = cryptoId,
                    cryptoName = cryptoName,
                    onSelect = { onCryptoSelected(cryptoId, cryptoName) },
                    onConfigureAlert = { cryptoId ->
                        onAlertConfigure(cryptoId)
                        // Vibración de confirmación
                        VibrationPattern.vibratePositive(context)
                    }
                )
            }
        }
    }
}

@Composable
fun CryptoCardWatch(
    cryptoId: String,
    cryptoName: String,
    onSelect: () -> Unit,
    onConfigureAlert: (cryptoId: String) -> Unit,
    context: android.content.Context,
    price: String = "---",
    variation: Double = 0.0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(320.dp)
            .padding(2.dp),
        elevation = CardElevationDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterStart
        ) {
            Text(
                text = cryptoName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = TextWeight.Normal
            )

            Text(
                text = cryptoId.toUpperCase(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                when (variation.sign()) {
                    1 -> Text(
                        text = "${variation.abs().formatted("%.1f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.error
                    )
                    -1 -> Text(
                        text = "${variation.abs().formatted("%.1f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.onSurface
                    )
                    else -> Text(
                        text = "0%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { onConfigureAlert(cryptoId) },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("A")
                }
            }

            Modifier.clickable(onClick = onSelect)
        }
    }
}