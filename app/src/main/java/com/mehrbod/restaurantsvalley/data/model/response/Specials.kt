package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Specials (

	@SerializedName("count") val count : Int,
	@SerializedName("items") val items : List<String>
)