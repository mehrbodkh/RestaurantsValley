package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class IconDto (

	@SerializedName("prefix") val prefix : String,
	@SerializedName("mapPrefix") val mapPrefix : String,
	@SerializedName("suffix") val suffix : String
)