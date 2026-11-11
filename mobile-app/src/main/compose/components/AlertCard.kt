package com.cryptowatch.mobile.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.TextWeight

@Composable
fun AlertCard(
    cryptoId: String,
    cryptoName: String,
    isActive: Boolean,
    onToggle: (cryptoId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardElevationDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = cryptoName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
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
            }

            Column(
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (isActive) "Activa" else "Inactiva",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = { onToggle(cryptoId) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(
                        text = if (isActive) "Desactivar" else "Activar",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}