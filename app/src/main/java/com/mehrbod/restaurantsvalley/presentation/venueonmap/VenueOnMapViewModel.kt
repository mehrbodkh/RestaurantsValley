package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.domain.model.Venue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueOnMapViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    private val _venuesState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
    val venuesState = _venuesState


    fun onMapCameraPositionUpdated(cameraPosition: CameraPosition) {
        viewModelScope.launch {
            venueRepository.getVenues(
                cameraPosition.target.latitude,
                cameraPosition.target.longitude,
                ((20 - cameraPosition.zoom) * 50).toInt()
            ).collect {
                if (it.isSuccess && it.getOrNull() != null) {
                    _venuesState.value = VenuesUiState.ShowVenues(it.getOrNull()!!)
                }
            }
        }
    }

}

sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class ShowVenues(val venues: List<Venue>) : VenuesUiState()
}