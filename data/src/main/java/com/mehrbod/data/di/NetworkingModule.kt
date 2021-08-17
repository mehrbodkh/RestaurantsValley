package com.mehrbod.data.di

import android.content.Context
import com.mehrbod.data.BuildConfig
import com.mehrbod.data.R
import com.mehrbod.data.api.RestaurantApiService
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
    fun provideClientId(): String = BuildConfig.CLIENT_ID

    @Provides
    @Named("ClientSecret")
    fun provideClientSecret(): String = BuildConfig.CLIENT_SECRET

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl(@ApplicationContext context: Context): String =
        context.getString(R.string.base_url)

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