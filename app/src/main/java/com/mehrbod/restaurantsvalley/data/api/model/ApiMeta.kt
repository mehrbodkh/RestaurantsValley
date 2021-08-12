package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiMeta (

	@SerializedName("code") val code : Int,
	@SerializedName("errorType") val errorType : String? = null,
	@SerializedName("errorDetail") val errorDetail : String? = null,
	@SerializedName("requestId") val requestId : String
)