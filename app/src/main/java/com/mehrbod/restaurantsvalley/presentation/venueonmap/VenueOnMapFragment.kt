package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mehrbod.map_module.MapModule
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.VenueOnMapFragmentBinding
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import com.mehrbod.restaurantsvalley.presentation.venueonmap.adapter.VenuesInfoAdapter
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.VenuesUiState
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

    private lateinit var infoAdapter: VenuesInfoAdapter

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
        initializeVenueObservers()
        initializeMapObservers()
    }

    @SuppressLint("MissingPermission")
    private fun initializeMap(savedInstanceState: Bundle?) {
        mapModule.initialize(binding.mapView, savedInstanceState) {
            mapModule.initializeLocationProvider(requireContext())
        }

        binding.myLocationButton.setOnClickListener {
            viewModel.onRequestLocationClicked()
        }

        binding.currentArea.setOnClickListener {
            mapModule.getCameraPosition()?.let {
                viewModel.onSearchAreaClicked(it.first.latitude, it.first.longitude, it.second)
            }
        }
    }

    private fun initializeInfoList() {
        infoAdapter = VenuesInfoAdapter {
            viewModel.onVenueClicked(it)
        }
        binding.venuesInfoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.venuesInfoList.adapter = infoAdapter
    }

    private fun initializeVenueObservers() {
        lifecycleScope.launch {
            viewModel.venuesState.collect {
                when (it) {
                    VenuesUiState.Loading -> showLoading()
                    is VenuesUiState.VenuesAvailable -> showVenues(it.restaurants)
                    is VenuesUiState.VenueDetailsAvailable -> showVenueDetail(it.bundle)
                    is VenuesUiState.Failure -> hideLoading()
                }
            }
        }
    }

    private fun initializeMapObservers() {
        lifecycleScope.launch {
            viewModel.locationState.collect {
                when (it) {
                    LocationUiState.Loading -> showLoading()
                    is LocationUiState.LocationAvailable -> handleLocationAvailable(it.location)
                    LocationUiState.Failure -> hideLoading()
                    is LocationUiState.GPSNeeded -> turnGpsOn(it.resolvableApiException)
                    LocationUiState.LocationPermissionNeeded -> grantLocationPermission()
                    LocationUiState.ShowLocationOnMap -> mapModule.initializeLocationProvider(
                        requireContext()
                    )
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showVenues(restaurants: List<Restaurant>) {
        hideLoading()
        showVenuesOnMap(restaurants)
        showVenuesInfo(restaurants)
    }

    private fun showVenueDetail(bundle: Bundle) {
        findNavController().navigate(R.id.action_venueOnMapFragment_to_venueDetailsFragment, bundle)
    }

    private fun showVenuesOnMap(restaurants: List<Restaurant>) {
        mapModule.removeAllMarkers()
        restaurants.forEach {
            mapModule.addMarker(
                it.id,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_on)!!,
                LatLng(it.location.lat, it.location.lng)
            )
        }
    }

    private fun showVenuesInfo(restaurants: List<Restaurant>) {
        infoAdapter.submitList(restaurants)
    }

    private fun handleLocationAvailable(location: Location) {
        hideLoading()
        mapModule.moveCamera(
            location.latitude,
            location.longitude,
            15.0
        )
        val userPosition = mapModule.getCameraPosition()
        userPosition?.let {
            viewModel.onUserLocationShowing(
                userPosition.first.latitude,
                userPosition.first.longitude,
                userPosition.second
            )
        }
    }

    private fun turnGpsOn(resolvableApiException: ResolvableApiException) {
        hideLoading()
        resolvableApiException.startResolutionForResult(requireActivity(), 1)
    }

    private fun grantLocationPermission() {
        hideLoading()
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.onPermissionResult(isGranted)
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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