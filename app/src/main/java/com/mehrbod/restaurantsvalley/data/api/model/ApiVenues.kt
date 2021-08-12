package com.mehrbod.restaurantsvalley.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiVenues (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val apiContact : ApiContact?,
    @SerializedName("location") val apiLocation : ApiLocation,
    @SerializedName("canonicalUrl") val canonicalUrl : String?,
    @SerializedName("canonicalPath") val canonicalPath : String?,
    @SerializedName("categories") val categories : List<ApiCategories>,
    @SerializedName("verified") val verified : Boolean?,
    @SerializedName("stats") val apiStats : ApiStats?,
    @SerializedName("beenHere") val apiBeenHere : ApiBeenHere?,
    @SerializedName("specials") val apiSpecials : ApiSpecials?,
    @SerializedName("venuePage") val apiVenuePage : ApiVenuePage?,
    @SerializedName("locked") val locked : Boolean?,
    @SerializedName("hereNow") val apiHereNow : ApiHereNow?,
    @SerializedName("referralId") val referralId : String?,
    @SerializedName("venueChains") val venueChains : List<String>?,
    @SerializedName("hasPerk") val hasPerk : Boolean?
)