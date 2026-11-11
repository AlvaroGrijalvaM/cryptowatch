package com.cryptowatch.mobile.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptocurrencies")
data class CryptoEntity(
    @PrimaryKey @ColumnInfo(name = "id") val cryptoId: String,
    @ColumnInfo(name = "name") val cryptoName: String,
    @ColumnInfo(name = "symbol") val cryptoSymbol: String,
    @ColumnInfo(name = "last_price_usd") val lastPriceUsd: Double,
    @ColumnInfo(name = "last_update") val lastUpdate: Long
)