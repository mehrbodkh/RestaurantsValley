package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Location (
    @SerializedName("address") val address : String,
    @SerializedName("crossStreet") val crossStreet : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lng") val lng : Double,
    @SerializedName("labeledLatLngs") val labeledLatLngs : List<LabeledLatLngs>,
    @SerializedName("distance") val distance : Int,
    @SerializedName("postalCode") val postalCode : Int,
    @SerializedName("cc") val cc : String,
    @SerializedName("neighborhood") val neighborhood : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("contextLine") val contextLine : String,
    @SerializedName("contextGeoId") val contextGeoId : Int,
    @SerializedName("formattedAddress") val formattedAddress : List<String>
)