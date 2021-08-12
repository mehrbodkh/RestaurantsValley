package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.api.model.ApiVenues
import com.mehrbod.restaurantsvalley.data.datasource.VenuesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class VenueRepositoryImpl @Inject constructor(
    @Named("RemoteSource") private val remoteDataSource: VenuesDataSource,
    @Named("IODispatcher") private val dispatcher: CoroutineDispatcher
) : VenueRepository {


    override fun getVenues(lat: Double, lng: Double, radius: Int): Flow<Result<List<ApiVenues>>> =
        flow {
            val result = remoteDataSource.fetchVenues(lat, lng, radius)
            if (result.apiMeta.code == 200) {
                emit(Result.success(result.apiResponse.venues))
            } else {
                emit(Result.failure<List<ApiVenues>>(Throwable("")))
            }
        }
            .flowOn(dispatcher)
            .catch { emit(Result.failure(it)) }
}