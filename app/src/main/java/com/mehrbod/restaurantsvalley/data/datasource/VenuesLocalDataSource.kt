package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Venue
import javax.inject.Inject
import javax.inject.Named

@Named("LocalDataSource")
class VenuesLocalDataSource @Inject constructor(
) : VenuesDataSource {
    companion object {
        const val SUCCESS = 200
        const val category = "4d4b7105d754a06374d81259"
    }

    override suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): Result<List<Venue>> {
        return Result.failure(Throwable(""))
    }

    override suspend fun fetchVenueDetail(venueId: String): Result<Venue> {
        return Result.failure(Throwable(""))
    }
}