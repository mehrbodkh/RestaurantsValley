package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiCategories (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("pluralName") val pluralName : String,
    @SerializedName("shortName") val shortName : String,
    @SerializedName("icon") val apiIcon : ApiIcon,
    @SerializedName("primary") val primary : Boolean
)