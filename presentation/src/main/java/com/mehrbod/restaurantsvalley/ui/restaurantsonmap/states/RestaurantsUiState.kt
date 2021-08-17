package com.mehrbod.restaurantsvalley.ui.restaurantsonmap.states

import com.mehrbod.domain.model.restaurant.Restaurant


sealed class RestaurantsUiState {
    object Loading : RestaurantsUiState()
    class RestaurantsAvailable(val restaurants: List<Restaurant>?) : RestaurantsUiState()
    class Failure(val message: String?) : RestaurantsUiState()
    class VenueDetailsAvailable(val key: String, val restaurantId: String) : RestaurantsUiState()
}