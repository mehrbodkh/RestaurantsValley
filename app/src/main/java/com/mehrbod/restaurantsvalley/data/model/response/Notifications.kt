package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Notifications (

	@SerializedName("type") val type : String,
	@SerializedName("item") val item : Item
)