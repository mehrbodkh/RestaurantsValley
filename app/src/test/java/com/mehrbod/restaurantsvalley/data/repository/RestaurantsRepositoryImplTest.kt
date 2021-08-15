package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.api.response.ApiVenuesResponse
import com.mehrbod.restaurantsvalley.data.datasource.RestaurantsRemoteDataSourceImpl
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsRepositoryImplTest {

    @RelaxedMockK
    lateinit var remoteDataSourceImpl: RestaurantsRemoteDataSourceImpl

    @RelaxedMockK
    lateinit var apiVenuesResponse: ApiVenuesResponse

    @RelaxedMockK
    lateinit var restaurants: List<Restaurant>

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var venueRepositoryImpl: RestaurantsRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coroutineDispatcher = TestCoroutineDispatcher()
        venueRepositoryImpl = RestaurantsRepositoryImpl(remoteDataSourceImpl, coroutineDispatcher)
    }

    @Test
    fun `test get venues successful response`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSourceImpl.fetchVenues(any(), any(), any()) } returns Result.success(restaurants)

        val response = venueRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()

        coVerify { remoteDataSourceImpl.fetchVenues(1.0, 1.0, 1) }
        assert(response.isSuccess)
        assert(response.getOrNull() != null)
        assert(response.getOrNull()!!.isNotEmpty())
    }

    @Test
    fun `test venues failed request`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSourceImpl.fetchVenues(any(), any(), any()) } throws Exception("")

        val response = venueRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()

        coVerify { remoteDataSourceImpl.fetchVenues(1.0, 1.0, 1) }
        assert(response.isFailure)
        assert(response.getOrNull() == null)
    }

    @Test
    fun `test venues failed response`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSourceImpl.fetchVenues(any(), any(), any()) } returns Result.failure(Throwable(""))

        val response = venueRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()

        coVerify { remoteDataSourceImpl.fetchVenues(1.0, 1.0, 1) }
        assert(response.isFailure)
        assert(response.getOrNull() == null)
    }

    @After
    fun finalize() {
        unmockkAll()
    }


}