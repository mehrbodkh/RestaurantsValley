package com.mehrbod.restaurantsvalley.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
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
    private val settingsClient = SettingsClient(context)
    private var permissionManager: PermissionsManager? = null

    fun isLocationPermissionGranted(): Boolean =
        PermissionsManager.areLocationPermissionsGranted(context)


    suspend fun isLocationEnabled(): Result<Boolean> = suspendCoroutine { continuation ->
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

    suspend fun findUserLocation(): Result<Location> {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }

        val permissionApproved =
            PermissionsManager.areLocationPermissionsGranted(context)

        return if (permissionApproved) {
            requestCurrentLocation(context)
        } else {
            Result.failure(Throwable("No permission"))
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

    fun requestLocationPermission(activity: Activity, completeListener: (Boolean) -> Unit) {
        permissionManager = PermissionsManager(object : PermissionsListener {
            override fun onExplanationNeeded(p0: MutableList<String>?) {}

            override fun onPermissionResult(isGranted: Boolean) {
                completeListener(isGranted)
            }
        })
        permissionManager?.requestLocationPermissions(activity)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}