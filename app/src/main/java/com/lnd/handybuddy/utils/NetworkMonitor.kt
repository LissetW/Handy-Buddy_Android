package com.lnd.handybuddy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

class NetworkMonitor(
    context: Context,
    private val onNetworkChanged: (Boolean) -> Unit
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkChanged(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkChanged(false)
        }
    }

    fun register() {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}