package com.mehrbod.domain.repository

import com.mehrbod.domain.model.restaurant.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantsRepository {
    fun getRestaurants(lat: Double, lng: Double, radius: Int): Flow<Result<List<Restaurant>>>
    fun getRestaurantDetails(restaurantId: String): Flow<Result<Restaurant>>
}