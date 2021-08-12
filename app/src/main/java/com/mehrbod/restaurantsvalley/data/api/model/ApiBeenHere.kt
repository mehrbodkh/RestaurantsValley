package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiBeenHere (
	@SerializedName("lastCheckinExpiredAt") val lastCheckinExpiredAt : Int
)