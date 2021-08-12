package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Icon (

	@SerializedName("prefix") val prefix : String,
	@SerializedName("mapPrefix") val mapPrefix : String,
	@SerializedName("suffix") val suffix : String
)