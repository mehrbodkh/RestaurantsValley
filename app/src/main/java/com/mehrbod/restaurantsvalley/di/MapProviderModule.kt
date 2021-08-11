package com.mehrbod.restaurantsvalley.di

import android.content.Context
import com.mapbox.mapboxsdk.maps.Style
import com.mehrbod.map_module.MapModule
import com.mehrbod.map_module.MapModuleImpl
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions
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
    fun provideMapToken(): String =
        "sk.eyJ1IjoibWVocmJvZCIsImEiOiJja3M3czJncmkwYWl2MnZuMDZ3cTQ2Y2p6In0.KCoKmygq7x8auJilSp6EGA"

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