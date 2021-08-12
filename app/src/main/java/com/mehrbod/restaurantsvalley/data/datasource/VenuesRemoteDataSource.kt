package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import com.mehrbod.restaurantsvalley.data.api.model.ApiVenuesResponse
import javax.inject.Inject
import javax.inject.Named

class VenuesRemoteDataSource @Inject constructor(
    private val apiService: RestaurantApiService,
    @Named("ClientId") private val clientId: String,
    @Named("ClientSecret") private val clientSecret: String
) : VenuesDataSource {
    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): ApiVenuesResponse {
        return apiService.getVenues(
            "$lat,$lng",
            radius,
            clientId = clientId,
            clientSecret = clientSecret
        )
    }
}