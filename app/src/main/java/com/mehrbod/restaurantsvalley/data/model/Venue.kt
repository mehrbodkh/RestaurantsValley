package com.mehrbod.restaurantsvalley.data.model

data class Venue(
    val id : String,
    val name : String,
    val apiContact : Contact?,
    val apiLocation : Location?,
    val canonicalUrl : String?,
    val canonicalPath : String?,
    val categories : List<Categories>,
    val verified : Boolean?,
    val apiStats : Stats?,
    val apiSpecials : Specials?,
    val apiVenuePage : VenuePage?,
    val locked : Boolean,
    val apiHereNow : HereNow?,
    val referralId : String,
    val venueChains : List<String>,
    val hasPerk : Boolean
)