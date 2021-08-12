package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.api.model.ApiVenuesResponse

interface VenuesDataSource {
    suspend fun fetchVenues(lat: Double, lng: Double, radius: Int): ApiVenuesResponse
}