package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.domain.model.Venue
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
                val location = locationResult.getOrNull()!!
                onSearchAreaClicked(location.latitude, location.longitude, 100)
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

}

sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class VenuesAvailable(val venues: List<Venue>) : VenuesUiState()
}

sealed class LocationUiState {
    object Loading : LocationUiState()
    object ShowLocationOnMap : LocationUiState()
    object LocationPermissionNeeded : LocationUiState()
    object Failure : LocationUiState()
    class GPSNeeded(val resolvableApiException: ResolvableApiException) : LocationUiState()
    class LocationAvailable(val location: Location) : LocationUiState()
}