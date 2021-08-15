package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Restaurant

interface RestaurantsLocalDataSource : RestaurantsDataSource {
    fun updateRestaurants(restaurants: List<Restaurant>)
    suspend fun getRestaurantDetail(restaurantId: String): Result<Restaurant>
}