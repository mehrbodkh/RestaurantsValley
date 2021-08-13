package com.mehrbod.restaurantsvalley.presentation.venueonmap

import androidx.lifecycle.ViewModel
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VenueOnMapViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

}