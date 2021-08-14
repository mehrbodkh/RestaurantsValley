package com.mehrbod.restaurantsvalley.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val AMSTERDAM_LAT = 52.370986
        private const val AMSTERDAM_LNG = 4.910211
        val DEFAULT_CAMERA_POSITION = LatLng(AMSTERDAM_LAT, AMSTERDAM_LNG)
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var cancellationTokenSource = CancellationTokenSource()

    suspend fun findUserLocation(): Result<Location> {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }

        val permissionApproved =
            PermissionsManager.areLocationPermissionsGranted(context)

        if (permissionApproved) {
            return requestCurrentLocation()
        } else {
            PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(p0: MutableList<String>?) {

                }

                override fun onPermissionResult(p0: Boolean) {

                }

            })
        }
        return Result.failure(Throwable("Something"))
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
                        continuation.resume(Result.failure(Throwable("Something went wrong")))
                    }
                }
            }
        }
}