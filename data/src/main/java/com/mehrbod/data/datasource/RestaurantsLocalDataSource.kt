package com.mehrbod.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant

interface RestaurantsLocalDataSource : RestaurantsDataSource {
    fun updateRestaurants(restaurants: List<Restaurant>)
    suspend fun getRestaurantDetail(restaurantId: String): Result<Restaurant>
}