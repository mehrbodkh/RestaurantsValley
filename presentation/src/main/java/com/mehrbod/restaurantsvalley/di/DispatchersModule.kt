package com.mehrbod.restaurantsvalley.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class DispatchersModule {

    @Provides
    @Named("IODispatcher")
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}