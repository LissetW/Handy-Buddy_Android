package com.lnd.handybuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey val sessionId: String,
    val technicianId: String,
    val technicianName: String,
    val technicianLastName: String,
    val technicianImage: String?,
    val lastMessage: String,
    val timestamp: Long
)