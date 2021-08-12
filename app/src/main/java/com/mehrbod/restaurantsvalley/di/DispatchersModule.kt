package com.mehrbod.restaurantsvalley.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    @Named("IODispatcher")
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}