package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class BeenHere (
	@SerializedName("lastCheckinExpiredAt") val lastCheckinExpiredAt : Int
)