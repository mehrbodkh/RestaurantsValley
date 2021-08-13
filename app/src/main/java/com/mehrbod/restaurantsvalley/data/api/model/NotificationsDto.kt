package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class NotificationsDto (

	@SerializedName("type") val type : String,
	@SerializedName("item") val itemDto : ItemDto
)