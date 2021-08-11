package com.mehrbod.restaurantsvalley.data.api

import retrofit2.http.GET

interface RestaurantApiService {

    @GET("venues/search")
    suspend fun getRestaurants()
}