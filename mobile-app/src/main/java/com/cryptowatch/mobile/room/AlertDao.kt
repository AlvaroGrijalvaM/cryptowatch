package com.cryptowatch.mobile.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): List<AlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(alert: AlertEntity)

    @Query("DELETE FROM alerts WHERE cryptoId = :cryptoId")
    fun deleteAlert(cryptoId: String)

    @Query("UPDATE alerts SET isActive = :isActive WHERE cryptoId = :cryptoId")
    fun updateAlertActiveStatus(cryptoId: String, isActive: Boolean)
}