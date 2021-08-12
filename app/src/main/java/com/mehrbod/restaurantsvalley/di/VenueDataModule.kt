package com.mehrbod.restaurantsvalley.di

import com.mehrbod.restaurantsvalley.data.datasource.VenuesDataSource
import com.mehrbod.restaurantsvalley.data.datasource.VenuesRemoteDataSource
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.data.repository.VenueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class VenueDataModule {

    @Binds
    @Named("RemoteSource")
    abstract fun bindsRemoteSource(
        venueRemoteDataSource: VenuesRemoteDataSource
    ): VenuesDataSource

    @Binds
    abstract fun bindsVenueRepository(
        venueRepositoryImpl: VenueRepositoryImpl
    ): VenueRepository
}