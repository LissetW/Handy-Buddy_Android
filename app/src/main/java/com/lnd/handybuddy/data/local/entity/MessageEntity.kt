package com.lnd.handybuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val timestamp: Long,
    val content: String
)