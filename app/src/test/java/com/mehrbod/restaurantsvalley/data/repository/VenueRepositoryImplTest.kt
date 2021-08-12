package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.restaurantsvalley.data.datasource.VenuesRemoteDataSource
import com.mehrbod.restaurantsvalley.data.model.response.VenuesResponse
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
    lateinit var venuesResponse: VenuesResponse

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
        every { venuesResponse.meta.code } returns 200
        coEvery { remoteDataSource.fetchVenues(any(), any(), any()) } returns venuesResponse
        val response = venueRepositoryImpl.getVenues(1.0, 1.0, 1).first()
        coVerify { remoteDataSource.fetchVenues(1.0, 1.0, 1) }
        assert(response.isSuccess)
        assert(response.getOrNull() != null)
        assert(response.getOrNull()!!.isEmpty())
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
        every { venuesResponse.meta.code } returns 400
        coEvery { remoteDataSource.fetchVenues(any(), any(), any()) } returns venuesResponse
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