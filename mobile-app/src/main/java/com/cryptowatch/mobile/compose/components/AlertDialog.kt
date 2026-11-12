package com.cryptowatch.mobile.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun ConfigureAlertDialog(
    cryptoId: String,
    cryptoName: String,
    currentPrice: Double,
    onDismiss: () -> Unit,
    onSaveAlert: (thresholdType: String, thresholdValue: Double) -> Unit
) {
    var thresholdType by remember { mutableStateOf("price") }
    var thresholdValueText by remember { mutableStateOf(if (currentPrice > 0) currentPrice.toString() else "0.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Configurar Alerta para ${cryptoName.ifEmpty { cryptoId.uppercase(Locale.ROOT) }}")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Precio actual: $${String.format(Locale.ROOT, "%.2f", currentPrice)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Tipo de umbral:",
                    style = MaterialTheme.typography.titleSmall
                )

                Column(Modifier.selectableGroup()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (thresholdType == "price"),
                            onClick = { thresholdType = "price" }
                        )
                        Text(
                            text = "Precio límite ($ USD)",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (thresholdType == "percentage"),
                            onClick = { thresholdType = "percentage" }
                        )
                        Text(
                            text = "Variación porcentual (%)",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = thresholdValueText,
                    onValueChange = { thresholdValueText = it },
                    label = {
                        Text(if (thresholdType == "price") "Precio objetivo ($ USD)" else "Porcentaje de cambio (%)")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = thresholdValueText.toDoubleOrNull() ?: 0.0
                    onSaveAlert(thresholdType, value)
                    onDismiss()
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
