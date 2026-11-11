package com.cryptowatch.watch

import android.content.Context
import android.os.Vibrator

object VibrationManager {
    fun vibrateAlert(context: Context, isPositive: Boolean) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!vibrator.hasVibrator) {
            return
        }

        // Patrones de vibración diferenciados
        // Vibración positiva: incremento de precio
        // Patrón: corto, pausa, medio
        // Vibración negativa: decremento de precio
        // Patrón: medio, pausa, corto

        when (isPositive) {
            true -> {
                // Vibración positiva: 3 toques rápidos
                // Patrón: 0ms silencio, 50ms vibrar, 50ms silencio, 50ms vibrar
                val positivePattern = longPressOf(0L, 50L, 50L, 50L)
                vibrator.vibrate(positivePattern, -1)
            }
            false -> {
                // Vibración negativa: 3 toques con diferente ritmo
                // Patrón: 0ms silencio, 50ms vibrar, 25ms silencio, 50ms vibrar
                val negativePattern = longPressOf(0L, 50L, 25L, 50L)
                vibrator.vibrate(negativePattern, -1)
            }
        }
    }

    // Función auxiliar para patrones de vibración
    // Recibe 4 valores long (tiempo en milisegundos: silencio, vibrar, silencio, vibrar)
    private fun longPressOf(pattern: LongArray): LongArray = pattern

    // Sobrecarga con parámetros explícitos para ayuda de tipo
    private fun longPressOf(
        silence1: Long,
        vibrate1: Long,
        silence2: Long,
        vibrate2: Long
    ): LongArray = longPressOf(longPressOf(silence1, vibrate1, silence2, vibrate2))
}