package com.mehrbod.restaurantsvalley.presentation.venueonmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun onMapCameraPositionUpdated(lat: Double, lng: Double, radius: Int) {
        viewModelScope.launch {
            venueRepository.getVenues(
                lat,
                lng,
                radius
            ).collect {
                _venuesState.value = VenuesUiState.ShowVenues(it.getOrNull() ?: emptyList())
            }
        }
    }

}

sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class ShowVenues(val venues: List<Venue>) : VenuesUiState()
}