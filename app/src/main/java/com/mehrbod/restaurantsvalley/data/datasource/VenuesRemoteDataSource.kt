package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import com.mehrbod.restaurantsvalley.data.model.response.VenuesResponse
import javax.inject.Inject

class VenuesRemoteDataSource @Inject constructor(
    private val apiService: RestaurantApiService
): VenuesDataSource {
    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): VenuesResponse {
        return apiService.getVenues("$lat,$lng", radius)
    }
}