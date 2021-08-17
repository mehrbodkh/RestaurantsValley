package com.mehrbod.restaurantsvalley.ui.restaurantsonmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.usecase.GetRestaurantsUseCase
import com.mehrbod.domain.usecase.GetUserLocationUseCase
import com.mehrbod.domain.usecase.LocationEnabledInfoUseCase
import com.mehrbod.domain.usecase.LocationPermissionGrantedInfoUseCase
import com.mehrbod.restaurantsvalley.ui.restaurantsdetails.RestaurantDetailsViewModel
import com.mehrbod.restaurantsvalley.ui.restaurantsonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.ui.restaurantsonmap.states.RestaurantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsOnMapViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val locationEnabledInfoUseCase: LocationEnabledInfoUseCase,
    private val locationPermissionGrantedInfoUseCase: LocationPermissionGrantedInfoUseCase
) : ViewModel() {

    private val _venuesState = MutableStateFlow<RestaurantsUiState>(RestaurantsUiState.Loading)
    val restaurantsState: StateFlow<RestaurantsUiState> = _venuesState

    private val _locationState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val locationState: StateFlow<LocationUiState> = _locationState

    init {
        viewModelScope.launch {
            handleLocation()
        }
    }

    fun onSearchAreaClicked(lat: Double, lng: Double, radius: Int) {
        viewModelScope.launch {
            _venuesState.value = RestaurantsUiState.Loading
            getRestaurantsUseCase.execute(
                lat,
                lng,
                radius
            ).collect { result ->
                if (result.getOrNull() != null) {
                    _venuesState.value = RestaurantsUiState.RestaurantsAvailable(result.getOrNull()!!)
                } else {
                    _venuesState.value = RestaurantsUiState.Failure(
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
        _venuesState.value = RestaurantsUiState.VenueDetailsAvailable(
            RestaurantDetailsViewModel.RESTAURANT_ID,
            restaurant.id
        )
    }

}
