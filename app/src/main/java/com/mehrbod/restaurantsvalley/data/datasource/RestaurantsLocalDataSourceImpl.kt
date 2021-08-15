package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import javax.inject.Inject
import javax.inject.Named

@Named("LocalDataSource")
class RestaurantsLocalDataSourceImpl @Inject constructor() : RestaurantsLocalDataSource {
    private val cachedRestaurants = HashSet<Restaurant>()

    override fun updateRestaurants(restaurants: List<Restaurant>) {
        cachedRestaurants.addAll(restaurants)
    }

    override suspend fun getRestaurantDetail(restaurantId: String): Result<Restaurant> {
        val restaurant = cachedRestaurants.find { it.id == restaurantId }
        return restaurant?.let {
            Result.success(it)
        } ?: Result.failure(Throwable("Nothing has been found"))
    }

    override suspend fun fetchRestaurants(
        lat: Double,
        lng: Double,
        radius: Int
    ): Result<List<Restaurant>> {
        return Result.failure(Throwable(""))
    }
}