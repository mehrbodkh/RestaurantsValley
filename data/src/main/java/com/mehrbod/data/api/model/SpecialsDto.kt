package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName

data class SpecialsDto (

	@SerializedName("count") val count : Int,
	@SerializedName("items") val items : List<String>
)