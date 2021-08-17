package com.mehrbod.domain.repository

import com.mehrbod.domain.model.restaurant.Restaurant
import kotlinx.coroutines.flow.Flow

/**
 * The implementation of this interface should be responsible for providing restaurants information
 * to use cases and should be implemented in the data layer.
 */
interface RestaurantsRepository {
    /**
     * It returns Result.success(listOf(restaurants)) when restaurants are available.
     * It returns Result.success(emptyList()) when there are no restaurants available.
     * It returns Result.failure() when it cannot be handled at the moment.
     */
    fun getRestaurants(lat: Double, lng: Double, radius: Int): Flow<Result<List<Restaurant>>>

    /**
     * It returns Result.success(restaurants) when restaurant details are available.
     * It returns Result.failure() when it cannot be handled at the moment.
     */
    fun getRestaurantDetails(restaurantId: String): Flow<Result<Restaurant>>
}