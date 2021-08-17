package com.mehrbod.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaserUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClientSecret

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher