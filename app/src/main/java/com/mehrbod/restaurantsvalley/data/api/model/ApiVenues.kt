package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiVenues (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val apiContact : ApiContact? = null,
    @SerializedName("location") val apiLocation : ApiLocation,
    @SerializedName("canonicalUrl") val canonicalUrl : String? = null,
    @SerializedName("canonicalPath") val canonicalPath : String? = null,
    @SerializedName("categories") val categories : List<ApiCategories>,
    @SerializedName("verified") val verified : Boolean? = null,
    @SerializedName("stats") val apiStats : ApiStats? = null,
    @SerializedName("beenHere") val apiBeenHere : ApiBeenHere? = null,
    @SerializedName("specials") val apiSpecials : ApiSpecials? = null,
    @SerializedName("venuePage") val apiVenuePage : ApiVenuePage? = null,
    @SerializedName("locked") val locked : Boolean? = null,
    @SerializedName("hereNow") val apiHereNow : ApiHereNow? = null,
    @SerializedName("referralId") val referralId : String? = null,
    @SerializedName("venueChains") val venueChains : List<String>? = null,
    @SerializedName("hasPerk") val hasPerk : Boolean? = null
)