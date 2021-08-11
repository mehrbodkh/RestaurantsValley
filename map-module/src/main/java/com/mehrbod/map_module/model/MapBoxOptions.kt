package com.mehrbod.map_module.model

import android.content.Context
import com.mapbox.mapboxsdk.maps.Style

data class MapBoxOptions(
    val applicationContext: Context,
    val token: String,
    val style: Style
) : MapOptions