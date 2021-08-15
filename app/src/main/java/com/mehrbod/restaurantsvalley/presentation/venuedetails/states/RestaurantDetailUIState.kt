package com.mehrbod.restaurantsvalley.presentation.venuedetails.states

import com.mehrbod.restaurantsvalley.domain.model.Restaurant

sealed class RestaurantDetailUIState {
    object Loading : RestaurantDetailUIState()
    class Failure(val message: String) : RestaurantDetailUIState()
    class RestaurantDetailAvailable(val restaurant: Restaurant) : RestaurantDetailUIState()
}