package com.lnd.handybuddy.application

import android.app.Application
import androidx.room.Room
import com.lnd.handybuddy.data.MessageRepository
import com.lnd.handybuddy.data.TechnicianRepository
import com.lnd.handybuddy.data.local.db.AppDatabase
import com.lnd.handybuddy.data.remote.RetrofitHelper


class HandyBuddyApp: Application() {
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "handybuddy-db").build()
    }

    val technicianRepository by lazy {
        TechnicianRepository(retrofit)
    }

    val messageRepository by lazy {
        MessageRepository(
            retrofit,
            database.chatSessionDao()
        )
    }
}