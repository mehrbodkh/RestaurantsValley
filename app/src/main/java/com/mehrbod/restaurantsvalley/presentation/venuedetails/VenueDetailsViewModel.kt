package com.mehrbod.restaurantsvalley.presentation.venuedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehrbod.restaurantsvalley.data.repository.RestaurantsRepository
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import com.mehrbod.restaurantsvalley.presentation.venuedetails.states.RestaurantDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository
) : ViewModel() {

    companion object {
        const val RESTAURANT_ID = "Restaurant Id"
    }

    private val _restaurantDetailUIState =
        MutableStateFlow<RestaurantDetailUIState>(RestaurantDetailUIState.Loading)
    val restaurantDetailUIState: StateFlow<RestaurantDetailUIState> = _restaurantDetailUIState

    fun onRestaurantIdReceived(restaurantId: String) {
        _restaurantDetailUIState.value = RestaurantDetailUIState.Loading
        viewModelScope.launch {
            restaurantsRepository.getRestaurantDetails(restaurantId).collect {
                if (it.getOrNull() != null) {

                    _restaurantDetailUIState.value =
                        RestaurantDetailUIState.RestaurantDetailAvailable(it.getOrNull()!!)
                } else {
                    _restaurantDetailUIState.value =
                        RestaurantDetailUIState.Failure("No restaurants found")
                }
            }
        }
    }

}
