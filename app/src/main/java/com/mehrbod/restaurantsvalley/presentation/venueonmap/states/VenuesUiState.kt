package com.mehrbod.restaurantsvalley.presentation.venueonmap.states

import android.os.Bundle
import com.mehrbod.restaurantsvalley.domain.model.Restaurant


sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class VenuesAvailable(val restaurants: List<Restaurant>) : VenuesUiState()
    class VenueDetailsAvailable(val bundle: Bundle) : VenuesUiState()
}