package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.api.model.ApiVenuesResponse
import com.mehrbod.restaurantsvalley.data.datasource.VenuesRemoteDataSource
import com.mehrbod.restaurantsvalley.data.model.Venue
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
class VenueRepositoryImplTest {

    @RelaxedMockK
    lateinit var remoteDataSource: VenuesRemoteDataSource

    @RelaxedMockK
    lateinit var apiVenuesResponse: ApiVenuesResponse

    @RelaxedMockK
    lateinit var venues: List<Venue>

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var venueRepositoryImpl: VenueRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coroutineDispatcher = TestCoroutineDispatcher()
        venueRepositoryImpl = VenueRepositoryImpl(remoteDataSource, coroutineDispatcher)
    }

    @Test
    fun `test get venues successful response`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSource.fetchVenues(any(), any(), any()) } returns Result.success(venues)

        val response = venueRepositoryImpl.getVenues(1.0, 1.0, 1).first()

        coVerify { remoteDataSource.fetchVenues(1.0, 1.0, 1) }
        assert(response.isSuccess)
        assert(response.getOrNull() != null)
        assert(response.getOrNull()!!.isNotEmpty())
    }

    @Test
    fun `test venues failed request`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSource.fetchVenues(any(), any(), any()) } throws Exception("")

        val response = venueRepositoryImpl.getVenues(1.0, 1.0, 1).first()

        coVerify { remoteDataSource.fetchVenues(1.0, 1.0, 1) }
        assert(response.isFailure)
        assert(response.getOrNull() == null)
    }

    @Test
    fun `test venues failed response`() = coroutineDispatcher.runBlockingTest {
        coEvery { remoteDataSource.fetchVenues(any(), any(), any()) } returns Result.failure(Throwable(""))

        val response = venueRepositoryImpl.getVenues(1.0, 1.0, 1).first()

        coVerify { remoteDataSource.fetchVenues(1.0, 1.0, 1) }
        assert(response.isFailure)
        assert(response.getOrNull() == null)
    }

    @After
    fun finalize() {
        unmockkAll()
    }


}