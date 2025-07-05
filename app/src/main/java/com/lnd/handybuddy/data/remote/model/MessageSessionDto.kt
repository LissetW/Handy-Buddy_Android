package com.lnd.handybuddy.data.remote.model

import com.google.gson.annotations.SerializedName
import com.lnd.handybuddy.utils.MessagesJsonKeys

data class MessageSessionDto(
    @SerializedName(MessagesJsonKeys.SESSION_ID)
    val sessionId: String,

    @SerializedName(MessagesJsonKeys.MESSAGES)
    val messages: List<MessageDto>
)
