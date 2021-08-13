package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class StatsDto (

	@SerializedName("tipCount") val tipCount : Int,
	@SerializedName("usersCount") val usersCount : Int,
	@SerializedName("checkinsCount") val checkinsCount : Int
)