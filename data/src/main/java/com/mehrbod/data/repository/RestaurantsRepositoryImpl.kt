package com.mehrbod.data.repository

import com.mehrbod.data.datasource.RestaurantsLocalDataSource
import com.mehrbod.data.datasource.RestaurantsRemoteDataSource
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
) : com.mehrbod.domain.repository.RestaurantsRepository {


    override fun getRestaurants(lat: Double, lng: Double, radius: Int): Flow<Result<List<com.mehrbod.domain.model.restaurant.Restaurant>>> =
        flow {
            val localData = localDataSource.fetchRestaurants(lat, lng, radius)
            localData.getOrNull()?.let {
                emit(localData)
            }
            val remoteData = remoteDataSource.fetchRestaurants(lat, lng, radius)
            emit(remoteData)
            if (remoteData.isSuccess) {
                localDataSource.updateRestaurants(remoteData.getOrDefault(emptyList()))
            }
        }
            .flowOn(dispatcher)
            .catch { emit(Result.failure(it)) }

    override fun getRestaurantDetails(restaurantId: String): Flow<Result<com.mehrbod.domain.model.restaurant.Restaurant>> = flow {
        emit(localDataSource.getRestaurantDetail(restaurantId))
    }
        .flowOn(dispatcher)
        .catch { emit(Result.failure(it)) }

}