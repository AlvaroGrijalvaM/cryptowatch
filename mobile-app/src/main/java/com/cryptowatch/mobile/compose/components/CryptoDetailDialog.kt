package com.cryptowatch.mobile.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.abs

@Composable
fun CryptoDetailDialog(
    cryptoId: String,
    cryptoName: String,
    currentPrice: Double,
    variation24h: Double,
    onDismiss: () -> Unit,
    onConfigureAlert: () -> Unit
) {
    var selectedTimeframe by remember { mutableStateOf("24h") }

    val variationMap = remember(variation24h) {
        mapOf(
            "24h" to variation24h,
            "7d" to variation24h * 1.5,
            "30d" to variation24h * 2.8,
            "1año" to variation24h * 5.2
        )
    }

    val currentVariation = variationMap[selectedTimeframe] ?: variation24h

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = cryptoName.ifEmpty { cryptoId.uppercase(Locale.ROOT) },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cryptoId.uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Price Display
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Precio USD",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "$${String.format(Locale.ROOT, "%.2f", currentPrice)}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        val isPos = currentVariation >= 0
                        val varSign = if (isPos) "+" else "-"
                        val color = if (isPos) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error

                        Text(
                            text = "$varSign${String.format(Locale.ROOT, "%.2f", abs(currentVariation))}% ($selectedTimeframe)",
                            style = MaterialTheme.typography.titleMedium,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Timeframe Selector Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("24h", "7d", "30d", "1año").forEach { tf ->
                        FilterChip(
                            selected = (selectedTimeframe == tf),
                            onClick = { selectedTimeframe = tf },
                            label = { Text(tf) }
                        )
                    }
                }

                // Mock Graphic Chart Representation
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Gráfico Histórico ($selectedTimeframe)",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.6f, 0.9f, 0.85f).forEach { heightFactor ->
                                val barColor = if (currentVariation >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(heightFactor)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(barColor)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onConfigureAlert()
                }
            ) {
                Text("Configurar Alerta")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
