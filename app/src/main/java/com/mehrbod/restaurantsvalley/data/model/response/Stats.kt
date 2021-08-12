package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Stats (

	@SerializedName("tipCount") val tipCount : Int,
	@SerializedName("usersCount") val usersCount : Int,
	@SerializedName("checkinsCount") val checkinsCount : Int
)