package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.datasource.VenuesDataSource
import com.mehrbod.restaurantsvalley.data.model.response.Venues
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class VenueRepositoryImpl @Inject constructor(
    @Named("RemoteSource") private val remoteDataSource: VenuesDataSource,
    @Named("IODispatcher") private val dispatcher: CoroutineDispatcher
) : VenueRepository {

    override fun getVenues(lat: Double, lng: Double, radius: Int): Flow<Result<List<Venues>>> =
        flow {
            try {
                val result = remoteDataSource.fetchVenues(lat, lng, radius)
                emit(Result.success(result.response.venues))
            } catch (e: Exception) {
                e.cause?.let {
                    emit(Result.failure<List<Venues>>(Throwable("Request did not have a response.")))
                }
            }
        }.flowOn(dispatcher)
}