package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.model.response.Venues
import kotlinx.coroutines.flow.Flow

interface VenueRepository {
    fun getVenues(lat: Double, lng: Double, radius: Int): Flow<Result<List<Venues>>>
}