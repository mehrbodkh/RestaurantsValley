package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.api.adapter.convertToVenues
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
        const val category = "4d4b7105d754a06374d81259"
    }

    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): Result<List<Venue>> {
        val response = apiService.getVenues(
            "$lat,$lng",
            radius,
            limit = 50,
            clientId = clientId,
            clientSecret = clientSecret,
            categoryIds = listOf(category)
        )

        return if (response.metaDto.code == SUCCESS) {
            Result.success(response.convertToVenues())
        } else {
            Result.failure(Throwable(response.metaDto.code.toString()))
        }
    }

    /**
     * Venue details API is not free and cannot be accessed via my account.
     *
     * This can be implemented for a more updated result.
     */
    override suspend fun fetchVenueDetail(venueId: String): Result<Venue> {
        return Result.failure(Throwable("API is not usable"))
    }
}