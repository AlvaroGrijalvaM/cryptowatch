package com.cryptowatch.watch.compose.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cryptowatch.watch.util.VibrationPattern
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun FavoritesScreen(
    onCryptoSelected: (cryptoId: String, cryptoName: String) -> Unit,
    onAlertConfigure: (cryptoId: String) -> Unit,
    context: Context
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        if (showSearch) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Button(
                onClick = { showSearch = true },
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text("🔍", style = MaterialTheme.typography.titleMedium)
            }
        }

        Text(
            text = "Favoritas",
            style = MaterialTheme.typography.titleLarge
        )

        val favoriteCryptos = listOf(
            Pair("btc", "Bitcoin"),
            Pair("eth", "Ethereum"),
            Pair("sol", "Solana")
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            items(favoriteCryptos.size) { index ->
                val (cryptoId, cryptoName) = favoriteCryptos[index]
                CryptoCardWatch(
                    cryptoId = cryptoId,
                    cryptoName = cryptoName,
                    onSelect = { onCryptoSelected(cryptoId, cryptoName) },
                    onConfigureAlert = { id ->
                        onAlertConfigure(id)
                        VibrationPattern.vibratePositive(context)
                    },
                    context = context
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
    context: Context,
    price: String = "---",
    variation: Double = 0.0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable(onClick = onSelect),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = cryptoName,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = cryptoId.uppercase(Locale.ROOT),
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                val formattedVar = String.format(Locale.ROOT, "%.1f", abs(variation))
                when (sign(variation).toInt()) {
                    1 -> Text(
                        text = "+$formattedVar%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    -1 -> Text(
                        text = "-$formattedVar%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    else -> Text(
                        text = "0%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { onConfigureAlert(cryptoId) },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("A")
                }
            }
        }
    }
}
