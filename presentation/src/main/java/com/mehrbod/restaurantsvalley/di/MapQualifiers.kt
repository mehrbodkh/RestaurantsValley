package com.mehrbod.restaurantsvalley.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MapboxToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MapStyleUrl
