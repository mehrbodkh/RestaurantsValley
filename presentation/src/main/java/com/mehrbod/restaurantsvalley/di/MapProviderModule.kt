package com.mehrbod.restaurantsvalley.di

import android.content.Context
import com.mapbox.mapboxsdk.maps.Style
import com.mehrbod.map_module.MapModule
import com.mehrbod.map_module.MapModuleImpl
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions
import com.mehrbod.restaurantsvalley.BuildConfig
import com.mehrbod.restaurantsvalley.util.LocationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
class MapProviderModule {

    @Provides
    @MapStyleUrl
    fun provideMapStyle(): String = Style.MAPBOX_STREETS

    @Provides
    @MapboxToken
    fun provideMapToken(): String = BuildConfig.MAPBOX_SDK_TOKEN

    @Provides
    fun provideMapModule(mapOptions: MapOptions): MapModule = MapModuleImpl(mapOptions)

    @Provides
    fun provideMapOptions(
        @ApplicationContext context: Context,
        @MapboxToken token: String,
        @MapStyleUrl styleUrl: String
    ): MapOptions {
        return MapBoxOptions(
            context,
            token,
            styleUrl,
            initialCameraPosition = LocationHelper.DEFAULT_CAMERA_POSITION
        )
    }
}