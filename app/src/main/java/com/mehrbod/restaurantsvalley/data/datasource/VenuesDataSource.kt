package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.model.response.VenuesResponse

interface VenuesDataSource {
    suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): VenuesResponse
}