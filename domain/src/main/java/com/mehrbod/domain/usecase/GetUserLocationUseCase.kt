package com.mehrbod.domain.usecase

import android.location.Location
import com.mehrbod.domain.repository.LocationRepository
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) : UseCase {
    suspend fun getUserLocation(): Result<Location> = locationRepository.findUserLocation()
}