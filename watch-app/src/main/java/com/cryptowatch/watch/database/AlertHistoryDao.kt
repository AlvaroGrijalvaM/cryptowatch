package com.cryptowatch.watch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlertHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: AlertHistoryEntity)

    @Query("SELECT * FROM alert_history WHERE crypto_id = :cryptoId ORDER BY triggered_at DESC LIMIT 50")
    suspend fun getRecentAlerts(cryptoId: String): List<AlertHistoryEntity>

    @Query("DELETE FROM alert_history WHERE triggered_at < :olderThan")
    suspend fun cleanOldAlerts(olderThan: Long)

    @Query("SELECT COUNT(*) FROM alert_history")
    suspend fun getAlertCount(): Int
}
