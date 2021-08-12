package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiHereNow (

	@SerializedName("count") val count : Int,
	@SerializedName("summary") val summary : String,
	@SerializedName("groups") val groups : List<String>
)