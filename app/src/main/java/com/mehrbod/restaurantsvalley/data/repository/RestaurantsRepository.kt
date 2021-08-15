package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantsRepository {
    fun getRestaurants(lat: Double, lng: Double, radius: Int): Flow<Result<List<Restaurant>>>
    fun getRestaurantDetails(restaurantId: String): Flow<Result<Restaurant>>
}