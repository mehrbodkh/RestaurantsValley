package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.domain.model.Venue
import kotlinx.coroutines.flow.Flow

interface VenueRepository {
    fun getVenues(lat: Double, lng: Double, radius: Int): Flow<Result<List<Venue>>>
    fun getVenueDetails(venueId: String): Flow<Result<Venue>>
}