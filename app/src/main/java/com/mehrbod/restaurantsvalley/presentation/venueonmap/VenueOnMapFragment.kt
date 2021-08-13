package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mehrbod.map_module.MapModule
import com.mehrbod.restaurantsvalley.databinding.VenueOnMapFragmentBinding
import com.mehrbod.restaurantsvalley.domain.model.Venue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class VenueOnMapFragment : Fragment() {

    @Inject
    lateinit var mapModule: MapModule

    @Inject
    @Named("MapStyleUrl")
    lateinit var mapStyleUrl: String

    private lateinit var viewModel: VenueOnMapViewModel

    private var _binding: VenueOnMapFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VenueOnMapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VenueOnMapViewModel::class.java)

        initializeMap(savedInstanceState)
        initializeObservers()
    }

    // TODO: All map related initializations should be moved to map module
    @SuppressLint("MissingPermission")
    private fun initializeMap(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            it.setStyle(mapStyleUrl)
            it.uiSettings.isRotateGesturesEnabled = false
            it.uiSettings.isTiltGesturesEnabled = false
            it.setMaxZoomPreference(17.0)
            it.setMinZoomPreference(6.0)
            it.cameraPosition = CameraPosition.Builder()
                .target(LatLng(52.370986, 4.910211))
                .zoom(12.0)
                .build()

            it.addOnCameraIdleListener {
                viewModel.onMapCameraPositionUpdated(it.cameraPosition)
            }

            if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
                it.getStyle { style ->
                    val locationComponent = it.locationComponent
                    val options =
                        LocationComponentActivationOptions.Builder(requireContext(), style).build()
                    locationComponent.activateLocationComponent(options)
                    locationComponent.isLocationComponentEnabled = true
                    locationComponent.renderMode = RenderMode.NORMAL
                }

            } else {
                PermissionsManager(object: PermissionsListener {
                    override fun onExplanationNeeded(p0: MutableList<String>?) {

                    }

                    override fun onPermissionResult(p0: Boolean) {
                        if (p0) {
                            it.getStyle { style ->
                                val locationComponent = it.locationComponent
                                val options =
                                    LocationComponentActivationOptions.Builder(requireContext(), style).build()
                                locationComponent.activateLocationComponent(options)
                                locationComponent.isLocationComponentEnabled = true
                                locationComponent.renderMode = RenderMode.NORMAL
                            }
                        }
                    }

                }).apply {
                    requestLocationPermissions(activity)
                }
            }
            binding.myLocationButton.setOnClickListener { _ ->
                if (it.locationComponent.isLocationComponentActivated) {
                    val lat = it.locationComponent.lastKnownLocation?.latitude
                    val lng = it.locationComponent.lastKnownLocation?.longitude
                    if (lat != null && lng != null) {
                        it.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(lat!!, lng!!))
                            .zoom(15.0)
                            .build()
                    }
                }

            }
        }
    }

    private fun initializeObservers() {
        lifecycleScope.launch {
            viewModel.venuesState.collect {
                when (it) {
                    VenuesUiState.Loading -> {
                    }
                    is VenuesUiState.ShowVenues -> showVenuesOnMap(it.venues)
                }
            }
        }
    }

    private fun showVenuesOnMap(venues: List<Venue>) {
        // TODO: Show venues on map
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }

}