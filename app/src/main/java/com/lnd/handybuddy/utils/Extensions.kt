package com.lnd.handybuddy.utils

import android.app.Activity
import android.widget.Toast

fun Activity.message(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}