package com.lnd.handybuddy.data

import com.lnd.handybuddy.data.remote.TechnicianApi
import com.lnd.handybuddy.data.remote.model.TechnicianDto
import retrofit2.Retrofit

class TechnicianRepository(
    private val retrofit: Retrofit
) {
    private val technicianApi = retrofit.create(TechnicianApi::class.java)
    suspend fun getTechnician(): List<TechnicianDto> = technicianApi.getTechnicians()
}