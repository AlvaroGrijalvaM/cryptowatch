package com.cryptowatch.mobile.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cryptoId: String,
    val cryptoName: String,
    val isActive: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)