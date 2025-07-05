package com.lnd.handybuddy.data

import com.lnd.handybuddy.data.local.dao.ChatSessionDao
import com.lnd.handybuddy.data.local.entity.ChatSessionEntity
import com.lnd.handybuddy.data.remote.MessageApi
import com.lnd.handybuddy.data.remote.model.MessageSessionDto
import retrofit2.Retrofit

class MessageRepository(
    retrofit: Retrofit,
    private val chatSessionDao: ChatSessionDao
)
{
    private val messageApi = retrofit.create(MessageApi::class.java)

    // Network
    suspend fun getMessageSessions(): List<MessageSessionDto> = messageApi.getMessageSessions()

    // Local
    suspend fun getLocalChatSessions(): List<ChatSessionEntity> = chatSessionDao.getAllChatSessions()

}