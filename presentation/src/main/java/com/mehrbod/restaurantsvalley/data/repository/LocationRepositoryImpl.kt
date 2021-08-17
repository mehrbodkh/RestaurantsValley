package com.mehrbod.restaurantsvalley.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mehrbod.data.repository.LocationRepository
import com.mehrbod.restaurantsvalley.util.locationPermissionNotGranted
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * This class implements LocationRepository from data layer to provide platform related location
 * info for the application usage.
 */
@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocationRepository {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var cancellationTokenSource = CancellationTokenSource()
    private val settingsClient = SettingsClient(context)

    override fun isLocationPermissionGranted(): Boolean =
        PermissionsManager.areLocationPermissionsGranted(context)


    override suspend fun isLocationEnabled(): Result<Boolean> = suspendCoroutine { continuation ->
        val locationRequestBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            LocationRequest.create()
                .setPriority(PRIORITY_HIGH_ACCURACY)
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
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }

        val permissionApproved =
            PermissionsManager.areLocationPermissionsGranted(context)

        return if (permissionApproved) {
            requestCurrentLocation(context)
        } else {
            Result.failure(locationPermissionNotGranted)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation(context: Context) =
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