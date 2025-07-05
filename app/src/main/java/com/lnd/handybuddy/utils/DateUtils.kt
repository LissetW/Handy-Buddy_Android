package com.lnd.handybuddy.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun parseIsoToMillis(isoDate: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(isoDate)?.time ?: 0L
}

fun formatTimestamp(timestamp: Long): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestamp

    val now = Calendar.getInstance()

    return when {
        // Si es el mismo día
        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) -> {
            DateFormat.format("hh:mm a", cal).toString()
        }

        // Si fue ayer
        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1 -> {
            "Yesterday"
        }

        // Si fue este año
        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) -> {
            DateFormat.format("MMM dd", cal).toString()
        }

        // Otro año
        else -> {
            DateFormat.format("MMM dd, yyyy", cal).toString()
        }
    }
}