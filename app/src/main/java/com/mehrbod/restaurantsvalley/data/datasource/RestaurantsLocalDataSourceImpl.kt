package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import javax.inject.Inject
import javax.inject.Named

@Named("LocalDataSource")
class RestaurantsLocalDataSourceImpl @Inject constructor() : RestaurantsLocalDataSource {

    override fun updateRestaurants(restaurants: List<Restaurant>) {
        TODO("Not yet implemented")
    }

    override suspend fun getRestaurantDetail(restaurantId: String): Result<Restaurant> {
        return Result.failure(Throwable(""))
    }

    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): Result<List<Restaurant>> {
        return Result.failure(Throwable(""))
    }
}