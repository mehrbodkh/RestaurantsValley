package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.data.api.RestaurantApiService
import com.mehrbod.restaurantsvalley.data.model.response.VenuesResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class VenuesRemoteDataSourceTest  {
    @MockK
    lateinit var apiService: RestaurantApiService

    @MockK
    @RelaxedMockK
    lateinit var venuesResponse: VenuesResponse

    private lateinit var remoteDataSource: VenuesDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteDataSource = VenuesRemoteDataSource(apiService)
    }

    @Test
    fun `test fetch venues call`() = runBlocking {
        coEvery { apiService.getVenues(allAny(), any()) } returns venuesResponse
        remoteDataSource.fetchVenues(1.0, 1.0, 1)
        coVerify { remoteDataSource.fetchVenues(1.0, 1.0, 1) }
        coVerify { apiService.getVenues(allAny(), any()) }
    }

    @After
    fun finalize() {
        unmockkAll()
    }
}