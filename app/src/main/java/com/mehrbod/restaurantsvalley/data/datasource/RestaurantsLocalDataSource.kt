package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant

interface RestaurantsLocalDataSource : RestaurantsDataSource {
    fun updateRestaurants(restaurants: List<com.mehrbod.domain.model.restaurant.Restaurant>)
    suspend fun getRestaurantDetail(restaurantId: String): Result<com.mehrbod.domain.model.restaurant.Restaurant>
}