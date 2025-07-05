package com.lnd.handybuddy.data.remote.model

import com.google.gson.annotations.SerializedName
import com.lnd.handybuddy.utils.TechnicianJsonKeys

data class TechnicianDto(
    @SerializedName(TechnicianJsonKeys.ID)
    var id: String? = null,

    @SerializedName(TechnicianJsonKeys.NAME)
    var name: String? = null,

    @SerializedName(TechnicianJsonKeys.LAST_NAME)
    var lastName: String? = null,

    @SerializedName(TechnicianJsonKeys.EMAIL)
    var email: String? = null,

    @SerializedName(TechnicianJsonKeys.IMAGE)
    var image: String? = null,

    @SerializedName(TechnicianJsonKeys.TYPE_OF_WORK)
    var typeOfWork: List<String>? = emptyList(),

    @SerializedName(TechnicianJsonKeys.WORK_DESCRIPTION)
    var workDescription: String? = null,

    @SerializedName(TechnicianJsonKeys.LOCATION)
    var location: LocationDto? = null,

    @SerializedName(TechnicianJsonKeys.SERVICE_RADIUS_KM)
    var serviceRadiusKM: Int? = 0,

    @SerializedName(TechnicianJsonKeys.PHONE_NUMBER)
    var phoneNumber: String? = null,

    @SerializedName(TechnicianJsonKeys.RATING_AVERAGE)
    var ratingAverage: Double? = 0.0,

    @SerializedName(TechnicianJsonKeys.NUMBER_OF_REVIEWS)
    var numberOfReviews: Int? = 0,

    @SerializedName(TechnicianJsonKeys.COMMENT_IDS)
    var commentIds: List<String> = emptyList()
)
