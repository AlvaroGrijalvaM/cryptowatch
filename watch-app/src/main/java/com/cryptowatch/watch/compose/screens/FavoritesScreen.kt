package com.cryptowatch.watch.compose.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryptowatch.watch.util.VibrationPattern
import java.util.Locale
import kotlin.math.abs

@Composable
fun FavoritesScreen(
    onCryptoSelected: (cryptoId: String, cryptoName: String) -> Unit,
    onAlertConfigure: (cryptoId: String) -> Unit,
    onOpenNotifications: () -> Unit,
    context: Context
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favoritas",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onOpenNotifications,
                    modifier = Modifier.size(28.dp)
                ) {
                    Text("🔔", fontSize = 12.sp)
                }

                IconButton(
                    onClick = { showSearch = !showSearch },
                    modifier = Modifier.size(28.dp)
                ) {
                    Text(if (showSearch) "✖" else "🔍", fontSize = 12.sp)
                }
            }
        }

        if (showSearch) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar...", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )
        }

        val favoriteCryptos = remember(searchQuery) {
            val all = listOf(
                Pair("btc", "Bitcoin"),
                Pair("eth", "Ethereum"),
                Pair("sol", "Solana")
            )
            if (searchQuery.isBlank()) all
            else all.filter { it.second.contains(searchQuery, ignoreCase = true) || it.first.contains(searchQuery, ignoreCase = true) }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cryptoName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = cryptoId.uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )

                val formattedVar = String.format(Locale.ROOT, "%.1f", abs(variation))
                val isPositive = variation >= 0
                val varColor = if (isPositive) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error

                Text(
                    text = "${if (isPositive) "+" else "-"}$formattedVar%",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = varColor,
                    modifier = Modifier.padding(end = 6.dp)
                )

                FilledTonalIconButton(
                    onClick = { onConfigureAlert(cryptoId) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Text("🔔", fontSize = 12.sp)
                }
            }
        }
    }
}
