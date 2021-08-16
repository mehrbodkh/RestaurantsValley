package com.mehrbod.domain.model.restaurant

data class Categories(
    val id : String,
    val name : String,
    val pluralName : String?,
    val shortName : String?,
    val icon : Icon?,
    val primary : Boolean?
)