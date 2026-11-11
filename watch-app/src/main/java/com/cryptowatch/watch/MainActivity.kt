package com.cryptowatch.watch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cryptowatch.watch.compose.screens.FavoritesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoWatchWatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FavoritesScreen(
                        onCryptoSelected = { _, _ -> },
                        onAlertConfigure = { _ -> },
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoWatchWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
