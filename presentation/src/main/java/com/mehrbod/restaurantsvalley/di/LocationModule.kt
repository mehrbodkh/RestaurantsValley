package com.mehrbod.restaurantsvalley.di

import com.mehrbod.domain.repository.LocationRepository
import com.mehrbod.data.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface LocationModule {

    @Binds
    fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}