package com.cryptowatch.watch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_configurations")
data class AlertConfigEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "crypto_id") val cryptoId: String,
    @ColumnInfo(name = "threshold_type") val thresholdType: String,
    @ColumnInfo(name = "threshold_value") val thresholdValue: Double,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long
)