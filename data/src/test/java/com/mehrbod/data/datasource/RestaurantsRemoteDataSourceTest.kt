package com.mehrbod.data.datasource

import com.mehrbod.data.api.RestaurantApiService
import com.mehrbod.data.api.model.MetaDto
import com.mehrbod.data.api.model.ResponseDto
import com.mehrbod.data.api.response.ApiVenuesResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RestaurantsRemoteDataSourceTest  {
    @MockK
    lateinit var apiService: RestaurantApiService

    private lateinit var remoteDataSource: RestaurantsRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteDataSource = RestaurantsRemoteDataSourceImpl(apiService, "null", "null")
    }

    @Test
    fun `test fetch venues api call`() = runBlocking {
        coEvery { apiService.getVenues(any(), any(), any(), any(), any(), any(), any()) } returns ApiVenuesResponse(
            MetaDto(code = 200, requestId = "32408243"),
            emptyList(),
            ResponseDto(venues = emptyList(), null)
        )
        val response = remoteDataSource.fetchRestaurants(1.0, 1.0, 1)
        coVerify { apiService.getVenues("1.0,1.0", 1, any(), any(), any(), any(), any()) }
        assert(response.isSuccess)
    }

    @Test
    fun `test fetch venues failure response`() = runBlocking {
        coEvery { apiService.getVenues(any(), any(), any(), any(), any(), any(), any()) } returns ApiVenuesResponse(
            MetaDto(code = 400, requestId = "32408243"),
            emptyList(),
            ResponseDto(venues = emptyList(), null)
        )
        val response = remoteDataSource.fetchRestaurants(1.0, 1.0, 1)
        coVerify { apiService.getVenues("1.0,1.0", 1, any(), any(), any(), any(), any()) }
        assert(response.isFailure)
    }

    @After
    fun finalize() {
        unmockkAll()
    }
}