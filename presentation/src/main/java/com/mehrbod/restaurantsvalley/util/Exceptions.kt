package com.mehrbod.restaurantsvalley.util

import java.lang.RuntimeException

val locationPermissionNotGranted = RuntimeException("No location permission")
val noRestaurantsFound = RuntimeException("No restaurants found")