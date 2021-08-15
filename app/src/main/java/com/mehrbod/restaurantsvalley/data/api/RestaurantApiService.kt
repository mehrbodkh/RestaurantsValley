package com.mehrbod.restaurantsvalley.data.api

import com.mehrbod.restaurantsvalley.data.api.response.ApiVenuesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {

    @GET("venues/search")
    suspend fun getVenues(
        @Query("ll") latLng: String,
        @Query("radius") radius: Int,
        @Query("v") version: String = "20180401",
        @Query("limit") limit: Int,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("categoryId") categoryIds: List<String>? = null
    ): ApiVenuesResponse
}