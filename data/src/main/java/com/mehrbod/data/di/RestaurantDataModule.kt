package com.mehrbod.data.di

import com.mehrbod.data.datasource.RestaurantsLocalDataSource
import com.mehrbod.data.datasource.RestaurantsLocalDataSourceImpl
import com.mehrbod.data.datasource.RestaurantsRemoteDataSource
import com.mehrbod.data.datasource.RestaurantsRemoteDataSourceImpl
import com.mehrbod.data.repository.RestaurantsRepositoryImpl
import com.mehrbod.domain.repository.RestaurantsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RestaurantDataModule {

    @Binds
    abstract fun bindsRemoteSource(
        restaurantsRemoteDataSourceImpl: RestaurantsRemoteDataSourceImpl
    ): RestaurantsRemoteDataSource

    @Binds
    abstract fun bindsLocalSource(
        restaurantsLocalDataSourceImpl: RestaurantsLocalDataSourceImpl
    ): RestaurantsLocalDataSource

    @Binds
    abstract fun bindsRestaurantsRepository(
        restaurantsRepositoryImpl: RestaurantsRepositoryImpl
    ): RestaurantsRepository
}