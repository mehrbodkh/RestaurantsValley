package com.mehrbod.restaurantsvalley.presentation.venuedetails

import androidx.lifecycle.ViewModel
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(
    private val venuesRepository: VenueRepository
) : ViewModel() {

    companion object {
        const val VENUE_ID = "Venue Id"
    }

    fun onVenueIdReceived(venueId: String) {

    }

}