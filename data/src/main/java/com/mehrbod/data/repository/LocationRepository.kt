package com.mehrbod.data.repository

import android.location.Location

/**
 * This interface should be implemented in presentation layer to provide location related
 * information to the use case.
 */
interface LocationRepository {
    /**
     * Checks whether location permission is granted or not. Returns true if it is granted and
     * returns false if it is not.
     */
    fun isLocationPermissionGranted(): Boolean

    /**
     * Checks whether the GPS is on or not.
     *
     * Returns Result.success(true) if it is on and if it's not, it returns
     * Result.failure(Exception). This exception can be used to provide a mean to simplify
     * turning on GPS for users.
     */
    suspend fun isLocationEnabled(): Result<Boolean>

    /**
     * Returns the current location of user. Returns Result.failure if cannot be resolved.
     */
    suspend fun findUserLocation(): Result<Location>
}