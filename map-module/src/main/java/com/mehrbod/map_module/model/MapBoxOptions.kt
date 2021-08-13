package com.mehrbod.map_module.model

import android.content.Context
import com.mapbox.mapboxsdk.geometry.LatLng

data class MapBoxOptions(
    val applicationContext: Context,
    val token: String,
    val styleUrl: String,
    val isRotateGestureEnabled: Boolean = false,
    val isTiltGestureEnabled: Boolean = false,
    val minZoomLevel: Double = 6.0,
    val maxZoomLevel: Double = 17.0,
    val defaultZoomLevel: Double = 12.0,
    val initialCameraPosition: LatLng,
) : MapOptions