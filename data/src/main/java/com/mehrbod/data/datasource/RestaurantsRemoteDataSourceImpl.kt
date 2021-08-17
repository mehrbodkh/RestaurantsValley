package com.mehrbod.data.datasource

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.data.api.adapter.convertToRestaurants
import com.mehrbod.data.api.RestaurantApiService
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
        const val SUCCESS_CODE = 200
        const val RESTAURANTS_CATEGORY = "4d4b7105d754a06374d81259"
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
            categoryIds = listOf(RESTAURANTS_CATEGORY)
        )

        return if (response.metaDto.code == SUCCESS_CODE) {
            Result.success(response.convertToRestaurants())
        } else {
            Result.failure(Throwable(response.metaDto.code.toString()))
        }
    }
}