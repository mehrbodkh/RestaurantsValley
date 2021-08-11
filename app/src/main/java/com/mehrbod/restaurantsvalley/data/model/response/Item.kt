package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Item (

	@SerializedName("unreadCount") val unreadCount : Int
)