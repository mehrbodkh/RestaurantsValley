package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName

data class ResponseDto (

    @SerializedName("venues") val venues : List<VenuesDto>,
    @SerializedName("confident") val confident : Boolean?
)