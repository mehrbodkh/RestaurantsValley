package com.mehrbod.data.datasource

import com.mehrbod.data.api.RestaurantApiService
import com.mehrbod.data.api.adapter.convertToRestaurants
import com.mehrbod.data.util.RESPONSE_LIMIT
import com.mehrbod.data.util.RESTAURANTS_CATEGORY_ID
import com.mehrbod.domain.model.restaurant.Restaurant
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
    }

    override suspend fun fetchRestaurants(
        lat: Double,
        lng: Double,
        radius: Int
    ): Result<List<Restaurant>> {
        val response = apiService.getVenues(
            "$lat,$lng",
            radius,
            limit = RESPONSE_LIMIT,
            clientId = clientId,
            clientSecret = clientSecret,
            categoryIds = listOf(RESTAURANTS_CATEGORY_ID)
        )

        return if (response.metaDto.code == SUCCESS_CODE) {
            Result.success(response.convertToRestaurants())
        } else {
            Result.failure(Throwable(response.metaDto.code.toString()))
        }
    }
}