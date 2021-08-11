package com.mehrbod.map_module

import com.mapbox.mapboxsdk.Mapbox
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions

class MapModuleImpl(options: MapOptions) : MapModule {
    init {
        (options as? MapBoxOptions)?.let { mapBoxOptions ->
            Mapbox
                .getInstance(mapBoxOptions.applicationContext, mapBoxOptions.token)
        }
    }
}