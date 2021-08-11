package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class HereNow (

	@SerializedName("count") val count : Int,
	@SerializedName("summary") val summary : String,
	@SerializedName("groups") val groups : List<String>
)