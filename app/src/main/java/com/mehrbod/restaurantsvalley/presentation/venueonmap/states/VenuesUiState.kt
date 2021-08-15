package com.mehrbod.restaurantsvalley.presentation.venueonmap.states

import com.mehrbod.restaurantsvalley.domain.model.Venue


sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class VenuesAvailable(val venues: List<Venue>) : VenuesUiState()
}