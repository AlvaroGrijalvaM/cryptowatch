package com.cryptowatch.mobile.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptocurrencies")
data class CryptoEntity(
    @PrimaryKey
    val cryptoId: String,
    val name: String,
    val symbol: String,
    @ColumnInfo(name = "last_price_usd") val lastPriceUsd: Double,
    @ColumnInfo(name = "last_update") val lastUpdate: Long = System.currentTimeMillis()
)
