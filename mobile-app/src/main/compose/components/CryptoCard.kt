package com.cryptowatch.mobile.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.TextWeight

@Composable
fun CryptoCard(
    cryptoId: String,
    cryptoName: String,
    onSelect: () -> Unit,
    onConfigureAlert: (cryptoId: String) -> Unit,
    price: String = "---",
    variation: Double = 0.0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(350.dp)
            .padding(4.dp),
        elevation = CardElevationDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
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
                    .padding(16.dp)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = TextWeight.Normal
            )

            Text(
                text = cryptoId.toUpperCase(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$$$$price",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                when (variation.sign()) {
                    1 -> Text(
                        text = "${variation.abs().formatted("%.2f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.colorScheme.error
                    )
                    -1 -> Text(
                        text = "${variation.abs().formatted("%.2f")}%",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.colorScheme.onSurface
                    )
                    else -> Text(
                        text = "0.00%",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { onConfigureAlert(cryptoId) },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("Alerta")
                }
            }

            Modifier.clickable(onClick = onSelect)
        }
    }
}