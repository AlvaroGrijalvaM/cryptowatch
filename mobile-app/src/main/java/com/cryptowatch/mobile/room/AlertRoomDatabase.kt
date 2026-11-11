package com.cryptowatch.mobile.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlertEntity::class], version = 1, exportSchemas = false)
abstract class AlertRoomDatabase : RoomDatabase() {
    abstract fun alertDao(): AlertDao
}