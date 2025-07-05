package com.lnd.handybuddy.data.remote.model

import com.google.gson.annotations.SerializedName
import com.lnd.handybuddy.utils.MessagesJsonKeys

data class MessageDto(
    @SerializedName(MessagesJsonKeys.ID)
    val id: String,

    @SerializedName(MessagesJsonKeys.SENDER)
    val sender: String,

    @SerializedName(MessagesJsonKeys.TIMESTAMP)
    val timestamp: String,

    @SerializedName(MessagesJsonKeys.CONTENT)
    val content: String
)
