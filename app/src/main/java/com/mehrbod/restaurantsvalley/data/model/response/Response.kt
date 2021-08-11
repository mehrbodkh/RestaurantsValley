package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Response (

	@SerializedName("venues") val venues : List<Venues>,
	@SerializedName("confident") val confident : Boolean
)