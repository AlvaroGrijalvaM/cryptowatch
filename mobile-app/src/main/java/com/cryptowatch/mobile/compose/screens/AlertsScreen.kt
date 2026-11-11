package com.cryptowatch.mobile.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptowatch.mobile.compose.components.AlertCard
import com.cryptowatch.mobile.room.AlertViewModel

@Composable
fun AlertsScreen(
    onBack: () -> Unit,
    onAlertToggled: (cryptoId: String, isActive: Boolean) -> Unit,
    alertViewModel: AlertViewModel = viewModel()
) {
    val alertConfigs by alertViewModel.alertConfigs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Alertas de Precio",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            if (alertConfigs.isEmpty()) {
                Text(
                    text = "No hay alertas configuradas",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(alertConfigs.size) { index ->
                        val config = alertConfigs[index]
                        AlertCard(
                            cryptoId = config.cryptoId,
                            cryptoName = config.cryptoName,
                            isActive = config.isActive,
                            onToggle = { id ->
                                alertViewModel.toggleAlert(id, config.isActive)
                                onAlertToggled(id, config.isActive)
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Volver")
        }
    }
}
