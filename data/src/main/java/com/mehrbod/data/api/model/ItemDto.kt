package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName

data class ItemDto (

	@SerializedName("unreadCount") val unreadCount : Int
)