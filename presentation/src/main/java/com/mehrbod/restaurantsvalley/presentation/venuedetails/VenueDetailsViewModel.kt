package com.mehrbod.restaurantsvalley.presentation.venuedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehrbod.domain.usecase.GetRestaurantDetailsUseCase
import kotlinx.coroutines.flow.collect
import com.mehrbod.restaurantsvalley.presentation.venuedetails.states.RestaurantDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(
    private val getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase
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
            getRestaurantDetailsUseCase.execute(restaurantId).collect {
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
