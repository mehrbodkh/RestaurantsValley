package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Restaurant

interface RestaurantsDataSource {
    suspend fun fetchRestaurants(lat: Double, lng: Double, radius: Int): Result<List<Restaurant>>
}