package com.lnd.handybuddy.data.remote

import com.lnd.handybuddy.data.remote.model.TechnicianDto
import retrofit2.http.GET

interface TechnicianApi {
    @GET("technician/all")
    suspend fun getTechnicians(): List<TechnicianDto>
}