package com.mehrbod.restaurantsvalley.ui.venueonmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.usecase.GetRestaurantsUseCase
import com.mehrbod.domain.usecase.GetUserLocationUseCase
import com.mehrbod.domain.usecase.LocationEnabledInfoUseCase
import com.mehrbod.domain.usecase.LocationPermissionGrantedInfoUseCase
import com.mehrbod.restaurantsvalley.ui.venuedetails.VenueDetailsViewModel
import com.mehrbod.restaurantsvalley.ui.venueonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.ui.venueonmap.states.VenuesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueOnMapViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val locationEnabledInfoUseCase: LocationEnabledInfoUseCase,
    private val locationPermissionGrantedInfoUseCase: LocationPermissionGrantedInfoUseCase
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
            getRestaurantsUseCase.execute(
                lat,
                lng,
                radius
            ).collect { result ->
                if (result.getOrNull() != null) {
                    _venuesState.value = VenuesUiState.VenuesAvailable(result.getOrNull()!!)
                } else {
                    _venuesState.value = VenuesUiState.Failure(
                        result.exceptionOrNull()?.message ?: "No results found"
                    )
                }
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

        if (!locationPermissionGrantedInfoUseCase.isLocationPermissionGranted()) {
            _locationState.value = LocationUiState.LocationPermissionNeeded
        } else if (locationEnabledInfoUseCase.isLocationEnabled().isFailure) {
            if (locationEnabledInfoUseCase.isLocationEnabled()
                    .exceptionOrNull() is ResolvableApiException
            ) {
                _locationState.value = LocationUiState.GPSNeeded(
                    locationEnabledInfoUseCase.isLocationEnabled()
                        .exceptionOrNull()!! as ResolvableApiException
                )
            } else {
                _locationState.value = LocationUiState.Failure
            }
        } else {
            val locationResult = getUserLocationUseCase.getUserLocation()

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

    fun onRestaurantClicked(restaurant: Restaurant) {
        _venuesState.value = VenuesUiState.VenueDetailsAvailable(
            VenueDetailsViewModel.RESTAURANT_ID,
            restaurant.id
        )
    }

}
