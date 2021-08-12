package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiSpecials (

	@SerializedName("count") val count : Int,
	@SerializedName("items") val items : List<String>
)