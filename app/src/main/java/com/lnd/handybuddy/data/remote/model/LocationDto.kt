package com.lnd.handybuddy.data.remote.model

import com.google.gson.annotations.SerializedName
import com.lnd.handybuddy.utils.TechnicianJsonKeys

data class LocationDto(
    @SerializedName(TechnicianJsonKeys.COUNTRY)
    val country: String? = null,
    @SerializedName(TechnicianJsonKeys.STATE)
    val state: String? = null,
    @SerializedName(TechnicianJsonKeys.CITY)
    val city: String? = null,
    @SerializedName(TechnicianJsonKeys.LATITUDE)
    val latitude: Double = 0.0,
    @SerializedName(TechnicianJsonKeys.LONGITUDE)
    val longitude: Double = 0.0,
    @SerializedName(TechnicianJsonKeys.POSTAL_CODE)
    val postalCode: String? = null
)
