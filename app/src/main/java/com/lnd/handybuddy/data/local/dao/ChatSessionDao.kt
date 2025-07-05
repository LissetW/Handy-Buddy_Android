package com.lnd.handybuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lnd.handybuddy.data.local.entity.ChatSessionEntity

@Dao
interface ChatSessionDao {
    @Query("SELECT * FROM chat_sessions ORDER BY timestamp DESC")
    suspend fun getAllChatSessions(): List<ChatSessionEntity>

    @Query("DELETE FROM chat_sessions")
    suspend fun clearChatSessions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatSessions(chats: List<ChatSessionEntity>)

    @Query("SELECT * FROM chat_sessions WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getChatSessionById(sessionId: String): ChatSessionEntity?
}