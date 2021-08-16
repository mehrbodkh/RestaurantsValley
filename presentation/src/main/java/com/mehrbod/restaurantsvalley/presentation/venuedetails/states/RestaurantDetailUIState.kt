package com.mehrbod.restaurantsvalley.presentation.venuedetails.states

import com.mehrbod.domain.model.restaurant.Restaurant

sealed class RestaurantDetailUIState {
    object Loading : RestaurantDetailUIState()
    class Failure(val message: String) : RestaurantDetailUIState()
    class RestaurantDetailAvailable(val restaurant: com.mehrbod.domain.model.restaurant.Restaurant) : RestaurantDetailUIState()
}