package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiResponse (

    @SerializedName("venues") val venues : List<ApiVenues>,
    @SerializedName("confident") val confident : Boolean
)