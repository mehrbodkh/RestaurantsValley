package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiNotifications (

	@SerializedName("type") val type : String,
	@SerializedName("item") val apiItem : ApiItem
)