package com.mehrbod.restaurantsvalley.data.api

import com.mehrbod.restaurantsvalley.data.model.response.VenuesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {

    @GET("venues/search")
    suspend fun getVenues(
        @Query("ll") latLng: String,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int
    ): VenuesResponse
}