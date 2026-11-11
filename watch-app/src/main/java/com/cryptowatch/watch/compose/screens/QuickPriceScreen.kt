package com.cryptowatch.watch.compose.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cryptowatch.watch.util.VibrationPattern
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun QuickPriceScreen(
    cryptoId: String,
    cryptoName: String,
    currentPrice: Double,
    variationPercent: Double,
    onAlertToggle: (cryptoId: String) -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cryptoName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = cryptoId.uppercase(Locale.ROOT),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = String.format(Locale.ROOT, "$%.2f", currentPrice),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                val formattedVar = String.format(Locale.ROOT, "%.1f", abs(variationPercent))
                when (sign(variationPercent).toInt()) {
                    1 -> Text(
                        text = "▲ $formattedVar%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    -1 -> Text(
                        text = "▼ $formattedVar%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    else -> Text(
                        text = "0.0%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        onAlertToggle(cryptoId)
                        VibrationPattern.vibratePositive(context)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Alerta")
                }
            }
        }
    }
}
