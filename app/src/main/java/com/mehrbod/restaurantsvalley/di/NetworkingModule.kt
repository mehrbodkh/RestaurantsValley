package com.mehrbod.restaurantsvalley.di

import android.content.Context
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

    @Provides
    @Named("ClientId")
    fun provideClientId(@ApplicationContext context: Context): String = context.getString(R.string.foursquare_client_id)

    @Provides
    @Named("ClientSecret")
    fun provideClientSecret(@ApplicationContext context: Context): String = context.getString(R.string.foursquare_client_secret)

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl(@ApplicationContext context: Context): String =
        context.getString(R.string.restaurants_base_api)

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofitClient(
        @Named("BaseUrl") baseUrl: String,
        gsonConverterFactory: GsonConverterFactory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideRestaurantApiService(retrofit: Retrofit): RestaurantApiService {
        return retrofit.create(RestaurantApiService::class.java)
    }
}