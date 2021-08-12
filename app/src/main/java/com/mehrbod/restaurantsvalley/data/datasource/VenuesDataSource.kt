package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.model.Venue

interface VenuesDataSource {
    suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): Result<List<Venue>>
}