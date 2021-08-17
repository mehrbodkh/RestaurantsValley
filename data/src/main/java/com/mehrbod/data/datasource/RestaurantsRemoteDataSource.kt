package com.mehrbod.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant

/**
 * Handles retrieving remote restaurants data.
 */
interface RestaurantsRemoteDataSource {
    /**
     * Returns Result.success(listOf(restaurants)) if available. Returns Result.failure() if
     * restaurants couldn't be retrieved.
     */
    suspend fun fetchRestaurants(lat: Double, lng: Double, radius: Int): Result<List<Restaurant>>
}