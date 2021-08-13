package com.mehrbod.map_module

import android.os.Bundle
import com.mapbox.mapboxsdk.maps.MapView

interface MapModule {
    fun initialize(mapView: MapView, savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun onSavedInstanceState(savedInstanceState: Bundle)
    fun onLowMemory()
}