package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class BeenHereDto (
	@SerializedName("lastCheckinExpiredAt") val lastCheckinExpiredAt : Int
)