package com.cryptowatch.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoWatchTheme {
                // A Surface wrapper for Material Theming
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO: Pantalla principal - lista de criptomonedas
                }
            }
        }
    }
}

@Composable
fun CryptoWatchTheme(content: @Composable () -> Unit) {
    var darkTheme by remember { mutableStateOf(true) }
    MaterialTheme.apply {
        if (darkTheme) {
            // Tema oscuro
        } else {
            // Tema claro
        }
    }
    content()
}