package com.mehrbod.restaurantsvalley.presentation.venueonmap.states

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException

sealed class LocationUiState {
    object Loading : LocationUiState()
    object ShowLocationOnMap : LocationUiState()
    object LocationPermissionNeeded : LocationUiState()
    object Failure : LocationUiState()
    class GPSNeeded(val resolvableApiException: ResolvableApiException) : LocationUiState()
    class LocationAvailable(val location: Location) : LocationUiState()
}