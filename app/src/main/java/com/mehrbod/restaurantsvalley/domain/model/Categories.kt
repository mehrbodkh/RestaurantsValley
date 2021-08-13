package com.mehrbod.restaurantsvalley.domain.model

data class Categories(
    val id : String,
    val name : String,
    val pluralName : String?,
    val shortName : String?,
    val icon : Icon?,
    val primary : Boolean?
)