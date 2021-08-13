package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.adapter.convertToVenues
import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import com.mehrbod.restaurantsvalley.domain.model.Venue
import javax.inject.Inject
import javax.inject.Named

class VenuesRemoteDataSource @Inject constructor(
    private val apiService: RestaurantApiService,
    @Named("ClientId") private val clientId: String,
    @Named("ClientSecret") private val clientSecret: String
) : VenuesDataSource {
    companion object {
        const val SUCCESS = 200
    }

    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): Result<List<Venue>> {
        val response = apiService.getVenues(
            "$lat,$lng",
            radius,
            clientId = clientId,
            clientSecret = clientSecret
        )

        return if (response.metaDto.code == SUCCESS) {
            Result.success(response.convertToVenues())
        } else {
            Result.failure(Throwable(response.metaDto.code.toString()))
        }
    }
}