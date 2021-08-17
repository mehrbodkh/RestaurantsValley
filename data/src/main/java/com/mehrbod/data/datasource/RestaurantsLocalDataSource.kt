package com.mehrbod.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant

/**
 * Handles local caching of restaurants data.
 */
interface RestaurantsLocalDataSource : RestaurantsDataSource {
    /**
     * This method can add all the passed data to the already existing cache of restaurants.
     *
     * Note that it doesn't remove older caches.
     */
    fun updateRestaurants(restaurants: List<Restaurant>)

    /**
     * It retrieves the details of a restaurant using its ID. Returns Result.success(restaurant) if
     * it is available in cache. Returns Result.failure otherwise.
     */
    suspend fun getRestaurantDetail(restaurantId: String): Result<Restaurant>
}