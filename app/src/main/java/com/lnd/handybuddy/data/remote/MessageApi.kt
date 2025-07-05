package com.lnd.handybuddy.data.remote

import com.lnd.handybuddy.data.remote.model.MessageSessionDto
import retrofit2.http.GET

interface MessageApi {
    @GET("messages")
    suspend fun getMessageSessions(): List<MessageSessionDto>
}