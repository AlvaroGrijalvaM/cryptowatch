package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptowatch.mobile.compose.components.AlertCard
import com.cryptowatch.mobile.database.AlertHistoryEntity
import com.cryptowatch.mobile.room.AlertViewModel
import java.util.Locale

@Composable
fun AlertsScreen(
    onBack: () -> Unit,
    onAlertToggled: (cryptoId: String, isActive: Boolean) -> Unit,
    alertViewModel: AlertViewModel = viewModel(),
    recentHistory: List<AlertHistoryEntity> = emptyList()
) {
    val alertConfigs by alertViewModel.alertConfigs.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Alertas y Notificaciones",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Configuradas (${alertConfigs.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Historial") }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedTab == 0) {
                if (alertConfigs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay alertas configuradas.\nPuedes añadir alertas desde la pantalla de Inicio o Búsqueda.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(alertConfigs.size) { index ->
                            val config = alertConfigs[index]
                            Column {
                                AlertCard(
                                    cryptoId = config.cryptoId,
                                    cryptoName = config.cryptoName,
                                    isActive = config.isActive,
                                    onToggle = { id ->
                                        alertViewModel.toggleAlert(id, config.isActive)
                                        onAlertToggled(id, config.isActive)
                                    }
                                )
                                Text(
                                    text = "Umbral: ${if (config.thresholdType == "price") "$${config.thresholdValue}" else "${config.thresholdValue}%"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                if (recentHistory.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay registro de alertas disparadas recientemente.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recentHistory.size) { index ->
                            val item = recentHistory[index]
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Alerta: ${item.cryptoId.uppercase(Locale.ROOT)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Precio alcanzado: $${String.format(Locale.ROOT, "%.2f", item.triggeredPrice)} | Variación: ${item.variationPercent}%",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Volver")
        }
    }
}
