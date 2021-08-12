package com.mehrbod.restaurantsvalley.data.model

data class Location(
    val address : String?,
    val crossStreet : String?,
    val lat : Double?,
    val lng : Double?,
    val distance : Int?,
    val postalCode : Int?,
    val cc : String?,
    val neighborhood : String?,
    val city : String?,
    val state : String?,
    val country : String?,
    val contextLine : String?,
    val contextGeoId : Int?,
    val formattedAddress : List<String>?
)