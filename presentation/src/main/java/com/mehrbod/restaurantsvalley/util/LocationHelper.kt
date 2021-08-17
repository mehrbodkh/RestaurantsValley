package com.mehrbod.restaurantsvalley.util

import com.mapbox.mapboxsdk.geometry.LatLng

class LocationHelper {
    companion object {
        private const val AMSTERDAM_LAT = 52.370986
        private const val AMSTERDAM_LNG = 4.910211
        val DEFAULT_CAMERA_POSITION = LatLng(AMSTERDAM_LAT, AMSTERDAM_LNG)
    }
}