package com.mehrbod.restaurantsvalley.data.datasource

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
        val result = cachedRestaurants.filter { it.inViewPort(lat, lng, radius) }.take(50)

        return if (result.isEmpty()) {
            Result.failure(Throwable("No cached data"))
        } else {
            Result.success(result)
        }
    }
}

private fun Restaurant.inViewPort(lat: Double, lng: Double, radius: Int): Boolean {
    return LatLng(lat, lng).distanceTo(LatLng(this.location.lat, this.location.lng)) <= radius
}