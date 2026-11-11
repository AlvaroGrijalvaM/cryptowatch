package com.cryptowatch.watch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_history")
data class AlertHistoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "crypto_id") val cryptoId: String,
    @ColumnInfo(name = "triggered_price") val triggeredPrice: Double,
    @ColumnInfo(name = "variation_percent") val variationPercent: Double,
    @ColumnInfo(name = "triggered_at") val triggeredAt: Long
)