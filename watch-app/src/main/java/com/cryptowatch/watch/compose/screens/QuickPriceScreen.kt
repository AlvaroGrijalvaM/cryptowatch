package com.cryptowatch.watch.compose.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryptowatch.watch.util.VibrationPattern
import java.util.Locale
import kotlin.math.abs

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
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = String.format(Locale.ROOT, "$%.2f", currentPrice),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                val formattedVar = String.format(Locale.ROOT, "%.1f", abs(variationPercent))
                val isPositive = variationPercent >= 0
                val varColor = if (isPositive) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error

                Text(
                    text = "${if (isPositive) "▲ +" else "▼ -"}$formattedVar%",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = varColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onAlertToggle(cryptoId)
                        VibrationPattern.vibratePositive(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(36.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("🔔 Alerta", fontSize = 12.sp)
                }
            }
        }
    }
}
