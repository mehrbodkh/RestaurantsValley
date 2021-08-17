package com.mehrbod.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.data.api.adapter.convertToRestaurants
import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RestaurantsRemoteDataSourceImpl @Inject constructor(
    private val apiService: RestaurantApiService,
    @Named("ClientId") private val clientId: String,
    @Named("ClientSecret") private val clientSecret: String
) : RestaurantsRemoteDataSource {
    companion object {
        const val SUCCESS = 200
        const val category = "4d4b7105d754a06374d81259"
    }

    override suspend fun fetchRestaurants(
        lat: Double,
        lng: Double,
        radius: Int
    ): Result<List<Restaurant>> {
        val response = apiService.getVenues(
            "$lat,$lng",
            radius,
            limit = 50,
            clientId = clientId,
            clientSecret = clientSecret,
            categoryIds = listOf(category)
        )

        return if (response.metaDto.code == SUCCESS) {
            Result.success(response.convertToRestaurants())
        } else {
            Result.failure(Throwable(response.metaDto.code.toString()))
        }
    }
}