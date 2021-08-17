package com.mehrbod.domain.usecase

import com.mehrbod.domain.repository.LocationRepository
import javax.inject.Inject

class LocationPermissionGrantedInfoUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) : UseCase {
    fun isLocationPermissionGranted(): Boolean = locationRepository.isLocationPermissionGranted()
}