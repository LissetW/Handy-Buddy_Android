package com.lnd.handybuddy.data.mappers

import com.lnd.handybuddy.data.local.entity.ChatSessionEntity
import com.lnd.handybuddy.data.remote.model.MessageSessionDto
import com.lnd.handybuddy.data.remote.model.TechnicianDto
import com.lnd.handybuddy.utils.parseIsoToMillis

fun MessageSessionDto.toEntity(technician: TechnicianDto?): ChatSessionEntity {
    val lastMessage = messages.lastOrNull()
    val technicianId = messages.firstOrNull { it.sender != "user" }?.sender ?: "unknown"

    return ChatSessionEntity(
        sessionId = sessionId,
        technicianId = technicianId,
        technicianName = technician?.name ?: "Unknown",
        technicianLastName = technician?.lastName ?: "",
        technicianImage = technician?.image,
        lastMessage = lastMessage?.content ?: "",
        timestamp = lastMessage?.timestamp?.let { parseIsoToMillis(it) } ?: 0L
    )
}
