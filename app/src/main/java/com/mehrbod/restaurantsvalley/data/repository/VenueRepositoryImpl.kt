package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.datasource.VenuesDataSource
import javax.inject.Inject
import javax.inject.Named

class VenueRepositoryImpl @Inject constructor(
    @Named("RemoteSource") remoteDataSource: VenuesDataSource
) : VenueRepository {

}