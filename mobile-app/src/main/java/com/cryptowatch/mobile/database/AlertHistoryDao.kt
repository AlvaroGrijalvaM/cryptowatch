package com.cryptowatch.mobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlertHistoryDao {
    @Insert(onConflict = OnConflictStrategy.KEEP)
    suspend fun insertHistory(history: AlertHistoryEntity)

    @Query("SELECT * FROM alert_history WHERE cryptoId = :cryptoId ORDER BY triggeredAt DESC LIMIT 50")
    suspend fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity>

    @Query("DELETE FROM alert_history WHERE triggeredAt < :olderThan")
    suspend fun cleanOldAlerts(olderThan: Long)

    @Query("SELECT COUNT(*) FROM alert_history")
    suspend fun getAlertCount(): Int
}