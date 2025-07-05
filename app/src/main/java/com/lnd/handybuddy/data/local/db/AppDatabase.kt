package com.lnd.handybuddy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lnd.handybuddy.data.local.dao.ChatSessionDao
import com.lnd.handybuddy.data.local.dao.MessageDao
import com.lnd.handybuddy.data.local.entity.ChatSessionEntity
import com.lnd.handybuddy.data.local.entity.MessageEntity

@Database(
    entities = [ChatSessionEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatSessionDao(): ChatSessionDao
    abstract fun messageDao(): MessageDao
}
