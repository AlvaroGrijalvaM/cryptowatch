package com.cryptowatch.watch.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.TextWeight

@Composable
fun QuickPriceScreen(
    cryptoId: String,
    cryptoName: String,
    currentPrice: Double,
    variationPercent: Double,
    onAlertToggle: (cryptoId: String) -> Unit,
    context: android.content.Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterStart
    ) {
        // Tarjeta compacta para vista rápida
        Card(
            modifier = Modifier
                .fillMaxWidth(300.dp)
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
                // Nombre y símbolo
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cryptoName,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(12.dp).fillMaxWidth(0.8f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = cryptoId.toUpperCase(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 4.dp)
                    )
                }

                // Precio principal
                Text(
                    text = currentPrice.formatted("%.2f"),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(12.dp)
                )

                // Variación con color diferenci
                when (variationPercent.sign()) {
                    1 -> Text(
                        text = "▲ ${variationPercent.abs().formatted("%.1f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.error
                    )
                    -1 -> Text(
                        text = "▼ ${variationPercent.abs().formatted("%.1f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.onSurface
                    )
                    else -> Text(
                        text = "0.0%",
                        style = MaterialTheme.typography.labelSmall,
                        style = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Botón de alerta flotante pequeño
                Button(
                    onClick = {
                        onAlertToggle(cryptoId)
                        // Vibración de confirmación
                        VibrationPattern.vibratePositive(context)
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.ui.platform.AndroidImageRestaurantKt.drawableRes(
                            androidx.core.R.drawable.ic_baseline_notifications_none_24
                        ),
                        contentDescription = "Alertas"
                    )
                }
            }
        }
    }
}