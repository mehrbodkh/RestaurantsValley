package com.mehrbod.data.api.response

import com.google.gson.annotations.SerializedName
import com.mehrbod.data.api.model.MetaDto
import com.mehrbod.data.api.model.NotificationsDto
import com.mehrbod.data.api.model.ResponseDto

data class ApiVenuesResponse (

    @SerializedName("meta") val metaDto : MetaDto,
    @SerializedName("notifications") val notifications : List<NotificationsDto>,
    @SerializedName("response") val responseDto : ResponseDto
)