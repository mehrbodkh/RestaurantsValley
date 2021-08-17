package com.mehrbod.data.datasource

import com.mehrbod.data.util.cacheDataNotFound
import com.mehrbod.data.util.noDetailsFound
import com.mehrbod.data.util.distance
import com.mehrbod.domain.model.restaurant.Restaurant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantsLocalDataSourceImpl @Inject constructor() : RestaurantsLocalDataSource {
    private val cachedRestaurants = HashSet<Restaurant>()

    override fun updateRestaurants(restaurants: List<Restaurant>) {
        cachedRestaurants.addAll(restaurants)
    }

    /**
     * These functions need to be suspendable if our cache wasn't just a simple memory set.
     */
    override fun getRestaurantDetail(restaurantId: String): Result<Restaurant> {
        val restaurant = cachedRestaurants.find { it.id == restaurantId }
        return restaurant?.let {
            Result.success(it)
        } ?: Result.failure(noDetailsFound)
    }

    /**
     * These functions need to be suspendable if our cache wasn't just a simple memory set.
     */
    override fun fetchRestaurants(
        lat: Double,
        lng: Double,
        radius: Int
    ): Result<List<Restaurant>> {
        val result = cachedRestaurants.filter {
            it.location.distance(lat, lng) <= radius
        }.take(50)

        return if (result.isEmpty()) {
            Result.failure(cacheDataNotFound)
        } else {
            Result.success(result)
        }
    }
}