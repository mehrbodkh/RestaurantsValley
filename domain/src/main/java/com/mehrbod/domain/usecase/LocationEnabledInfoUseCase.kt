package com.mehrbod.domain.usecase

import com.mehrbod.domain.repository.LocationRepository
import javax.inject.Inject

class LocationEnabledInfoUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend fun isLocationEnabled(): Result<Boolean> = locationRepository.isLocationEnabled()
}