package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant

interface RestaurantsDataSource {
    suspend fun fetchRestaurants(lat: Double, lng: Double, radius: Int): Result<List<com.mehrbod.domain.model.restaurant.Restaurant>>
}