package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.domain.model.Venue
import com.mehrbod.restaurantsvalley.presentation.venuedetails.VenueDetailsViewModel
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.VenuesUiState
import com.mehrbod.restaurantsvalley.util.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueOnMapViewModel @Inject constructor(
    private val venueRepository: VenueRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _venuesState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
    val venuesState: StateFlow<VenuesUiState> = _venuesState

    private val _locationState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val locationState: StateFlow<LocationUiState> = _locationState

    init {
        viewModelScope.launch {
            handleLocation()
        }
    }

    fun onSearchAreaClicked(lat: Double, lng: Double, radius: Int) {
        viewModelScope.launch {
            _venuesState.value = VenuesUiState.Loading
            venueRepository.getVenues(
                lat,
                lng,
                radius
            ).collect {
                _venuesState.value = VenuesUiState.VenuesAvailable(it.getOrNull() ?: emptyList())
            }
        }
    }

    fun onRequestLocationClicked() {
        viewModelScope.launch {
            handleLocation()
        }
    }

    private suspend fun handleLocation() {
        _locationState.value = LocationUiState.Loading

        if (!locationHelper.isLocationPermissionGranted()) {
            _locationState.value = LocationUiState.LocationPermissionNeeded
        } else if (locationHelper.isLocationEnabled().isFailure) {
            if (locationHelper.isLocationEnabled().exceptionOrNull() is ResolvableApiException) {
                _locationState.value = LocationUiState.GPSNeeded(
                    locationHelper.isLocationEnabled().exceptionOrNull()!! as ResolvableApiException
                )
            } else {
                _locationState.value = LocationUiState.Failure
            }
        } else {
            val locationResult = locationHelper.findUserLocation()

            if (locationResult.isSuccess) {
                _locationState.value =
                    LocationUiState.LocationAvailable(locationResult.getOrNull()!!)
            } else {
                _locationState.value = LocationUiState.Failure
            }
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            _locationState.value = LocationUiState.ShowLocationOnMap
            viewModelScope.launch {
                handleLocation()
            }
        }
    }

    fun onUserLocationShowing(lat: Double, lng: Double, radius: Int) {
        onSearchAreaClicked(lat, lng, radius)
    }

    fun onVenueClicked(venue: Venue) {
        _venuesState.value = VenuesUiState.VenueDetailsAvailable(Bundle().apply {
            putString(
                VenueDetailsViewModel.VENUE_ID,
                venue.id
            )
        })
    }

}
