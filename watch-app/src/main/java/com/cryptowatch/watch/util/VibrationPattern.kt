package com.cryptowatch.watch.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibrationPattern {
    val POSITIVE: LongArray = longArrayOf(0, 100, 200)
    val NEGATIVE: LongArray = longArrayOf(200, 100, 200)
    val NEUTRAL: LongArray = longArrayOf(0)

    fun vibrate(context: Context, pattern: LongArray) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
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
