package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiVenuesResponse (

    @SerializedName("meta") val apiMeta : ApiMeta,
    @SerializedName("notifications") val notifications : List<ApiNotifications>,
    @SerializedName("response") val apiResponse : ApiResponse
)