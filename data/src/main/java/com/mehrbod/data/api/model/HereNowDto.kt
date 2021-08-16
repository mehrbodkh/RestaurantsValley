package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName

data class HereNowDto (

	@SerializedName("count") val count : Int?,
	@SerializedName("summary") val summary : String?,
	@SerializedName("groups") val groups : List<Any>?
)