package com.mehrbod.restaurantsvalley.data.model.response

import com.google.gson.annotations.SerializedName

data class Venues (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val contact : Contact,
    @SerializedName("location") val location : Location,
    @SerializedName("canonicalUrl") val canonicalUrl : String,
    @SerializedName("canonicalPath") val canonicalPath : String,
    @SerializedName("categories") val categories : List<Categories>,
    @SerializedName("verified") val verified : Boolean,
    @SerializedName("stats") val stats : Stats,
    @SerializedName("beenHere") val beenHere : BeenHere,
    @SerializedName("specials") val specials : Specials,
    @SerializedName("venuePage") val venuePage : VenuePage,
    @SerializedName("locked") val locked : Boolean,
    @SerializedName("hereNow") val hereNow : HereNow,
    @SerializedName("referralId") val referralId : String,
    @SerializedName("venueChains") val venueChains : List<String>,
    @SerializedName("hasPerk") val hasPerk : Boolean
)