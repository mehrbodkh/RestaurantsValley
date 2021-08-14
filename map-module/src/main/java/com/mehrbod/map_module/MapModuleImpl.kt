package com.mehrbod.map_module

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mehrbod.map_module.model.MapBoxOptions
import com.mehrbod.map_module.model.MapOptions

class MapModuleImpl(private val options: MapOptions) : MapModule {
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null

    private val addedLayers = ArrayList<Layer>()

    init {
        (options as? MapBoxOptions)?.let { mapBoxOptions ->
            Mapbox
                .getInstance(mapBoxOptions.applicationContext, mapBoxOptions.token)
        }
    }

    override fun initialize(
        mapView: MapView,
        savedInstanceState: Bundle?,
        onMapReadyListener: () -> Unit
    ) {
        this.mapView = mapView

        (options as? MapBoxOptions)?.let { mapBoxOptions ->
            onCreate(savedInstanceState)
            mapView.getMapAsync {
                this.mapboxMap = it
                initializeMapboxMap(mapBoxOptions)
                onMapReadyListener()
            }
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

    override fun addOnCameraIdleListener(listener: (position: LatLng, radius: Int) -> Unit) {
        mapboxMap?.let {
            it.addOnCameraIdleListener {
                val cameraPosition = it.cameraPosition
                val radius = it.projection.visibleRegion.farLeft
                    .distanceTo(it.projection.visibleRegion.farRight)

                listener(
                    LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude),
                    radius.toInt()
                )
            }
        }
    }

    override fun addMarker(tag: String, icon: Drawable, position: LatLng) {
        val feature =
            Feature.fromGeometry(Point.fromLngLat(position.longitude, position.latitude))
        feature.addStringProperty("MARKER_ID_KEY", tag)
        val addMarkerSource = GeoJsonSource(tag, feature)

        mapboxMap?.style?.let { style ->
            if (style.getSource(tag) == null) {
                style.addImage(tag, icon)
                style.addSource(addMarkerSource)
            } else {
                (style.getSource(tag) as GeoJsonSource).setGeoJson(feature)
            }
            if (style.getLayer(tag) == null) {
                val addMarkerSymbolLayer = SymbolLayer(tag, tag)
                    .withProperties(
                        PropertyFactory.iconSize(1.0f),
                        PropertyFactory.iconOpacity(1f),
                        PropertyFactory.iconImage(tag),
                        PropertyFactory.iconAllowOverlap(true),
                    )
                addMarkerSymbolLayer.minZoom =
                    (options as? MapBoxOptions)?.minZoomLevel?.toFloat() ?: 4F
                style.addLayer(addMarkerSymbolLayer)
                addedLayers.add(addMarkerSymbolLayer)
            } else {
                style.getLayer(tag)?.setProperties()
            }
        }
    }

    override fun removeAllMarkers() {
        mapboxMap?.style?.let { style ->
            addedLayers.forEach {
                style.removeLayer(it)
                style.removeSource(it.id)
                style.removeImage(it.id)
            }

            addedLayers.clear()
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