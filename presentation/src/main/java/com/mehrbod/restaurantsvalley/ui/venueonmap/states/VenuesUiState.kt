package com.mehrbod.restaurantsvalley.ui.venueonmap.states


sealed class VenuesUiState {
    object Loading : VenuesUiState()
    class VenuesAvailable(val restaurants: List<com.mehrbod.domain.model.restaurant.Restaurant>) : VenuesUiState()
    class Failure(val message: String) : VenuesUiState()
    class VenueDetailsAvailable(val key: String, val restaurantId: String) : VenuesUiState()
}