package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiMeta (

	@SerializedName("code") val code : Int,
	@SerializedName("requestId") val requestId : String
)