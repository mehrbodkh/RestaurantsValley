package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class LabeledLatLngsDto (

	@SerializedName("label") val label : String,
	@SerializedName("lat") val lat : Double,
	@SerializedName("lng") val lng : Double
)