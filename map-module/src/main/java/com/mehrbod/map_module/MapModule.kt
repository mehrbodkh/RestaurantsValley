package com.mehrbod.map_module

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView

interface MapModule {
    fun initialize(mapView: MapView, savedInstanceState: Bundle?, onMapReadyListener: () -> Unit)
    fun addOnCameraIdleListener(listener: (position: LatLng, radius: Int) -> Unit)
    fun addMarker(tag: String, icon: Drawable, position: LatLng)
    fun removeAllMarkers()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun onSavedInstanceState(savedInstanceState: Bundle)
    fun onLowMemory()
}