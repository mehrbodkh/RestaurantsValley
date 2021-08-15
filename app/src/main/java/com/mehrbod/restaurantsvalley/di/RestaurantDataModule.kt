package com.mehrbod.restaurantsvalley.di

import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsLocalDataSource
import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsLocalDataSourceImpl
import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsRemoteDataSource
import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsRemoteDataSourceImpl
import com.mehrbod.restaurantsvalley.data.repository.RestaurantsRepository
import com.mehrbod.restaurantsvalley.data.repository.RestaurantsRepositoryImpl
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