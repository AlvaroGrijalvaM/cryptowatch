package com.cryptowatch.mobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportMapFunctions

@Database(entities = [CryptoEntity::class, AlertConfigEntity::class, AlertHistoryEntity::version], version = 1)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun cryptoDao(): CryptoDao
    abstract fun alertConfigDao(): AlertConfigDao
    abstract fun alertHistoryDao(): AlertHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoDatabase? = null

        fun getDatabase(context: Context): CryptoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoDatabase::class.java,
                    "crypto_watch_db"
                ).build()
            }
        }
    }
}