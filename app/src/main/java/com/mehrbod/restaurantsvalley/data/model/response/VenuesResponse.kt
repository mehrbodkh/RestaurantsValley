package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class VenuesResponse (

	@SerializedName("meta") val meta : Meta,
	@SerializedName("notifications") val notifications : List<Notifications>,
	@SerializedName("response") val response : Response
)