package com.mehrbod.data.di

import android.content.Context
import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideClientId(@ApplicationContext context: Context): String =
        "DJL2RZAD1XD2QBXSEBLKCVT4FPRZIOOJXARXRNVVVUVEGEGF"

    @Provides
    @Named("ClientSecret")
    fun provideClientSecret(@ApplicationContext context: Context): String =
        "KIJ5KGCMV554PA5HUS54Z4VSN1K0U3Z5LRJ1LKLSQW3OISIX"

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl(): String =
        "https://api.foursquare.com/v2/"

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