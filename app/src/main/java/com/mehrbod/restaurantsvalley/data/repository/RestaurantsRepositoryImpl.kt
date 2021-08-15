package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsLocalDataSource
import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsRemoteDataSource
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class RestaurantsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RestaurantsRemoteDataSource,
    private val localDataSource: RestaurantsLocalDataSource,
    @Named("IODispatcher") private val dispatcher: CoroutineDispatcher
) : RestaurantsRepository {


    override fun getRestaurants(lat: Double, lng: Double, radius: Int): Flow<Result<List<Restaurant>>> =
        flow {
            val result = remoteDataSource.fetchRestaurants(lat, lng, radius)
            emit(result)
            if (result.isSuccess) {
                localDataSource.updateRestaurants(result.getOrDefault(emptyList()))
            }
        }
            .flowOn(dispatcher)
            .catch { emit(Result.failure(it)) }

    override fun getRestaurantDetails(restaurantId: String): Flow<Result<Restaurant>> = flow {
        emit(localDataSource.getRestaurantDetail(restaurantId))
    }
        .flowOn(dispatcher)
        .catch { emit(Result.failure(it)) }

}