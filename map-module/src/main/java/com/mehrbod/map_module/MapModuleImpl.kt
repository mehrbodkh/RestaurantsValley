package com.mehrbod.map_module

import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions

class MapModuleImpl(private val options: MapOptions) : MapModule {
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null

    init {
        (options as? MapBoxOptions)?.let { mapBoxOptions ->
            Mapbox
                .getInstance(mapBoxOptions.applicationContext, mapBoxOptions.token)
        }
    }

    override fun initialize(mapView: MapView, savedInstanceState: Bundle?) {
        this.mapView = mapView

        (options as? MapBoxOptions)?.let { mapBoxOptions ->
            initializeMapboxView(mapBoxOptions, savedInstanceState)
        }
    }

    private fun initializeMapboxView(mapBoxOptions: MapBoxOptions, savedInstanceState: Bundle?) {
        onCreate(savedInstanceState)
        mapView?.getMapAsync {
            this.mapboxMap = it
            initializeMapboxMap(mapBoxOptions)
        }
    }

    private fun initializeMapboxMap(mapBoxOptions: MapBoxOptions) {
        mapboxMap?.let {
            it.setStyle(mapBoxOptions.styleUrl)
            it.uiSettings.isRotateGesturesEnabled = mapBoxOptions.isRotateGestureEnabled
            it.uiSettings.isTiltGesturesEnabled = mapBoxOptions.isTiltGestureEnabled
            it.setMinZoomPreference(mapBoxOptions.minZoomLevel)
            it.setMaxZoomPreference(mapBoxOptions.maxZoomLevel)
            it.cameraPosition = CameraPosition.Builder()
                .target(mapBoxOptions.initialCameraPosition)
                .zoom(mapBoxOptions.defaultZoomLevel)
                .build()
        }
    }

    private fun onCreate(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
    }

    override fun onStart() {
        mapView?.onStart()
    }

    override fun onResume() {
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        mapView = null
        mapboxMap = null
    }

    override fun onSavedInstanceState(savedInstanceState: Bundle) {
        mapView?.onSaveInstanceState(savedInstanceState)
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
    }
}