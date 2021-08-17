package com.mehrbod.restaurantsvalley.ui.venuedetails.states

sealed class RestaurantDetailUIState {
    object Loading : RestaurantDetailUIState()
    class Failure(val message: String) : RestaurantDetailUIState()
    class RestaurantDetailAvailable(val restaurant: com.mehrbod.domain.model.restaurant.Restaurant) : RestaurantDetailUIState()
}