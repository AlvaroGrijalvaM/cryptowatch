package com.cryptowatch.watch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CryptoEntity::class, AlertConfigEntity::class, AlertHistoryEntity::class], version = 1, exportSchema = false)
abstract class CryptoWatchDatabase : RoomDatabase() {

    abstract fun cryptoDao(): CryptoDao
    abstract fun alertConfigDao(): AlertConfigDao
    abstract fun alertHistoryDao(): AlertHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoWatchDatabase? = null

        fun getDatabase(context: Context): CryptoWatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoWatchDatabase::class.java,
                    "crypto_watch_watch_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
