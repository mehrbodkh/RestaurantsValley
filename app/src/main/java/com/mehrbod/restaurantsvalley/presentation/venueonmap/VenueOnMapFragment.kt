package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mehrbod.map_module.MapModule
import com.mehrbod.restaurantsvalley.R
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

    private val infoAdapter = VenuesInfoAdapter()

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
        initializeInfoList()
        initializeObservers()
    }

    // TODO: All map related initializations should be moved to map module
    @SuppressLint("MissingPermission")
    private fun initializeMap(savedInstanceState: Bundle?) {
        mapModule.initialize(binding.mapView, savedInstanceState) {
            mapModule.addOnCameraIdleListener { position, radius ->
                viewModel.onMapCameraPositionUpdated(position.latitude, position.longitude, radius)
            }
        }
//            if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
//                it.getStyle { style ->
//                    val locationComponent = it.locationComponent
//                    val options =
//                        LocationComponentActivationOptions.Builder(requireContext(), style).build()
//                    locationComponent.activateLocationComponent(options)
//                    locationComponent.isLocationComponentEnabled = true
//                    locationComponent.renderMode = RenderMode.NORMAL
//                }
//
//            } else {
//                PermissionsManager(object: PermissionsListener {
//                    override fun onExplanationNeeded(p0: MutableList<String>?) {
//
//                    }
//
//                    override fun onPermissionResult(p0: Boolean) {
//                        if (p0) {
//                            it.getStyle { style ->
//                                val locationComponent = it.locationComponent
//                                val options =
//                                    LocationComponentActivationOptions.Builder(requireContext(), style).build()
//                                locationComponent.activateLocationComponent(options)
//                                locationComponent.isLocationComponentEnabled = true
//                                locationComponent.renderMode = RenderMode.NORMAL
//                            }
//                        }
//                    }
//
//                }).apply {
//                    requestLocationPermissions(activity)
//                }
//            }
//            binding.myLocationButton.setOnClickListener { _ ->
//                if (it.locationComponent.isLocationComponentActivated) {
//                    val lat = it.locationComponent.lastKnownLocation?.latitude
//                    val lng = it.locationComponent.lastKnownLocation?.longitude
//                    if (lat != null && lng != null) {
//                        it.cameraPosition = CameraPosition.Builder()
//                            .target(LatLng(lat!!, lng!!))
//                            .zoom(15.0)
//                            .build()
//                    }
//                }
//
//            }
//        }
    }

    private fun initializeInfoList() {
        binding.venuesInfoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.venuesInfoList.adapter = infoAdapter
    }

    private fun initializeObservers() {
        lifecycleScope.launch {
            viewModel.venuesState.collect {
                when (it) {
                    VenuesUiState.Loading -> {
                    }
                    is VenuesUiState.VenuesAvailable -> showVenues(it.venues)
                }
            }
        }
    }

    private fun showVenues(venues: List<Venue>) {
        showVenuesOnMap(venues)
        showVenuesInfo(venues)
    }

    private fun showVenuesOnMap(venues: List<Venue>) {
        mapModule.removeAllMarkers()
        venues.forEach {
            mapModule.addMarker(
                it.id,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_on)!!,
                LatLng(it.location.lat, it.location.lng)
            )
        }
    }

    private fun showVenuesInfo(venues: List<Venue>) {
        infoAdapter.submitList(venues)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapModule.onSavedInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapModule.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapModule.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapModule.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapModule.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapModule.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapModule.onDestroy()
        _binding = null
    }

}