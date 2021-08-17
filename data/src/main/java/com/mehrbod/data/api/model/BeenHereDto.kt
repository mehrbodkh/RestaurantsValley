package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName

data class BeenHereDto (
	@SerializedName("lastCheckinExpiredAt") val lastCheckinExpiredAt : Int
)