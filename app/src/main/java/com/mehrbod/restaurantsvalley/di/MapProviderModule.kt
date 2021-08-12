package com.mehrbod.restaurantsvalley.di

import android.content.Context
import com.mapbox.mapboxsdk.maps.Style
import com.mehrbod.map_module.MapModule
import com.mehrbod.map_module.MapModuleImpl
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions
import com.mehrbod.restaurantsvalley.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class MapProviderModule {

    @Provides
    @Named("MapStyleUrl")
    fun provideMapStyle(): String = Style.MAPBOX_STREETS

    @Provides
    @Named("MapboxToken")
    fun provideMapToken(@ApplicationContext context: Context): String =
        context.getString(R.string.mapbox_sdk_token)

    @Provides
    fun provideMapModule(mapOptions: MapOptions): MapModule = MapModuleImpl(mapOptions)

    @Provides
    fun provideMapOptions(
        @ApplicationContext context: Context,
        @Named("MapboxToken") token: String,
        @Named("MapStyleUrl") styleUrl: String
    ): MapOptions {
        return MapBoxOptions(context, token, styleUrl)
    }
}