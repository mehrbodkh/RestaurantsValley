package com.mehrbod.restaurantsvalley.domain.model

data class Restaurant(
    val id : String,
    val name : String,
    val contact : Contact?,
    val location : Location,
    val canonicalUrl : String?,
    val canonicalPath : String?,
    val categories : List<Categories>,
    val verified : Boolean?,
    val stats : Stats?,
    val specials : Specials?,
    val apiVenuePage : VenuePage?,
    val locked : Boolean?,
    val hereNow : HereNow?,
    val referralId : String?,
    val venueChains : List<String>?,
    val hasPerk : Boolean?
)