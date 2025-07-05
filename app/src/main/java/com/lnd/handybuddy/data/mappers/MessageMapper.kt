package com.lnd.handybuddy.data.mappers

import com.lnd.handybuddy.data.local.entity.MessageEntity
import com.lnd.handybuddy.data.remote.model.MessageDto
import com.lnd.handybuddy.utils.parseIsoToMillis

fun MessageDto.toEntity(sessionId: String): MessageEntity {
    return MessageEntity(
        id = id,
        sessionId = sessionId,
        timestamp = parseIsoToMillis(timestamp),
        content = content
    )
}