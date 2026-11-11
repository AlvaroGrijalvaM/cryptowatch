package com.cryptowatch.mobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CryptoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: CryptoEntity)

    @Query("SELECT * FROM cryptocurrencies WHERE cryptoId = :cryptoId")
    suspend fun getCryptoById(cryptoId: String): CryptoEntity?

    @Query("SELECT * FROM cryptocurrencies")
    suspend fun getAllCryptos(): List<CryptoEntity>

    @Query("DELETE FROM cryptocurrencies")
    suspend fun clearAllCryptos()

    @Query("UPDATE cryptocurrencies SET last_price_usd = :price, last_update = :timestamp WHERE cryptoId = :cryptoId")
    suspend fun updateCryptoPrice(cryptoId: String, price: Double, timestamp: Long)
}