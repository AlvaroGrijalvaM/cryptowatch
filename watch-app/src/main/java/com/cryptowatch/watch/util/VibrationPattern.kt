package com.cryptowatch.watch.util

import android.content.Context
import androidx.core.content.ContextCompat

object VibrationPattern {
    // Patrón de vibración positivo: corto-largo-corto (incremento)
    val POSITIVE: LongArray = longArrayOf(0, 100, 200)
    
    // Patrón de vibración negativo: largo-corto-largo (disminución)
    val NEGATIVE: LongArray = longArrayOf(200, 100, 200)
    
    // Patrón de vibración neutra/none
    val NEUTRAL: LongArray = longArrayOf(0)

    fun vibrate(context: Context, pattern: LongArray) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        if (vibrator.hasVibrator) {
            vibrator.vibrate(pattern, -1)
        }
    }

    fun vibratePositive(context: Context) {
        vibrate(context, POSITIVE)
    }

    fun vibrateNegative(context: Context) {
        vibrate(context, NEGATIVE)
    }

    fun vibrateNeutral(context: Context) {
        vibrate(context, NEUTRAL)
    }
}