@file:JvmName("LocationExtensions")

package com.mehrbod.data.util

import com.mehrbod.domain.model.restaurant.Location

fun Location.distance(lat: Double, lng: Double): Int {
    val startLocation = android.location.Location("StartLocation").apply {
        latitude = this@distance.lat
        longitude = this@distance.lng
    }

    val endLocation = android.location.Location("EndLocation").apply {
        latitude = lat
        longitude = lng
    }

    return startLocation.distanceTo(endLocation).toInt()
}