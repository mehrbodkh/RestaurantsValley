package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiMeta (

	@SerializedName("code") val code : Int,
	@SerializedName("errorType") val errorType : String?,
	@SerializedName("errorDetail") val errorDetail : String?,
	@SerializedName("requestId") val requestId : String
)