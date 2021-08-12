package com.mehrbod.restaurantsvalley.data.model

data class Categories(
    val id : String,
    val name : String,
    val pluralName : String?,
    val shortName : String?,
    val apiIcon : Icon?,
    val primary : Boolean?
)