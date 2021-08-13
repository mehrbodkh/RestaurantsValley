package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ContactDto (

	@SerializedName("phone") val phone : Int,
	@SerializedName("formattedPhone") val formattedPhone : String
)