package com.mehrbod.data.api.model

import com.google.gson.annotations.SerializedName
import com.mehrbod.data.api.model.*

data class VenuesDto (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val contactDto : ContactDto? = null,
    @SerializedName("location") val locationDto : LocationDto,
    @SerializedName("canonicalUrl") val canonicalUrl : String? = null,
    @SerializedName("canonicalPath") val canonicalPath : String? = null,
    @SerializedName("categories") val categories : List<CategoriesDto>,
    @SerializedName("verified") val verified : Boolean? = null,
    @SerializedName("stats") val statsDto : StatsDto? = null,
    @SerializedName("beenHere") val beenHereDto : BeenHereDto? = null,
    @SerializedName("specials") val specialsDto : SpecialsDto? = null,
    @SerializedName("venuePage") val venuePageDto : VenuePageDto? = null,
    @SerializedName("locked") val locked : Boolean? = null,
    @SerializedName("hereNow") val hereNowDto : HereNowDto? = null,
    @SerializedName("referralId") val referralId : String? = null,
    @SerializedName("venueChains") val venueChains : List<String>? = null,
    @SerializedName("hasPerk") val hasPerk : Boolean? = null
)