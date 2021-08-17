package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName
import com.mehrbod.data.api.model.ItemDto

data class NotificationsDto (

	@SerializedName("type") val type : String,
	@SerializedName("item") val itemDto : ItemDto
)