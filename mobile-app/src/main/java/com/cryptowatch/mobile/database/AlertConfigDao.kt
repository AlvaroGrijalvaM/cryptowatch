package com.cryptowatch.mobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: AlertConfigEntity)

    @Query("SELECT * FROM alert_configurations WHERE crypto_id = :cryptoId")
    suspend fun getConfigByCrypto(cryptoId: String): AlertConfigEntity?

    @Query("SELECT * FROM alert_configurations")
    suspend fun getAllConfigs(): List<AlertConfigEntity>

    @Query("SELECT * FROM alert_configurations")
    fun getAllConfigsFlow(): Flow<List<AlertConfigEntity>>

    @Query("DELETE FROM alert_configurations WHERE crypto_id = :cryptoId")
    suspend fun deleteConfig(cryptoId: String)

    @Update
    suspend fun updateConfig(config: AlertConfigEntity)

    @Query("UPDATE alert_configurations SET is_active = :isActive WHERE crypto_id = :cryptoId")
    suspend fun setActive(cryptoId: String, isActive: Boolean)
}
