package com.mehrbod.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.mapbox.android.core.permissions.PermissionsManager
import com.mehrbod.data.util.locationPermissionNotGranted
import com.mehrbod.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * This class implements LocationRepository from domain layer to provide platform related location
 * info for the application usage.
 */
@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocationRepository {
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var cancellationTokenSource = CancellationTokenSource()
    private val settingsClient = SettingsClient(context)

    override fun isLocationPermissionGranted(): Boolean =
        PermissionsManager.areLocationPermissionsGranted(context)


    override suspend fun isLocationEnabled(): Result<Boolean> = suspendCoroutine { continuation ->
        val locationRequestBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            LocationRequest.create().setPriority(PRIORITY_HIGH_ACCURACY)
        )
        val locationRequest = locationRequestBuilder.build()

        settingsClient.checkLocationSettings(locationRequest).apply {
            addOnSuccessListener {
                continuation.resume(Result.success(true))
            }

            addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
        }
    }

    override suspend fun findUserLocation(): Result<Location> {
        return if (PermissionsManager.areLocationPermissionsGranted(context)) {
            requestCurrentLocation()
        } else {
            Result.failure(locationPermissionNotGranted)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation() =
        suspendCoroutine<Result<Location>> { continuation ->
            if (PermissionsManager.areLocationPermissionsGranted(context)) {
                val currentLocationTask: Task<Location>? = fusedLocationClient?.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

                currentLocationTask?.addOnCompleteListener { task: Task<Location> ->
                    if (task.isSuccessful && task.result != null) {
                        val result: Location = task.result
                        continuation.resume(Result.success(result))
                    } else {
                        val exception = task.exception
                        continuation.resume(Result.failure(exception!!))
                    }
                }
            }
        }
}