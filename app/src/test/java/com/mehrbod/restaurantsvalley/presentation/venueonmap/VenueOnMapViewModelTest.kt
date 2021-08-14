package com.mehrbod.restaurantsvalley.presentation.venueonmap

import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.domain.model.Venue
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VenueOnMapViewModelTest {

    @RelaxedMockK
    lateinit var venueRepository: VenueRepository

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: VenueOnMapViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coroutineDispatcher = TestCoroutineDispatcher()
        viewModel = VenueOnMapViewModel(venueRepository)
        Dispatchers.setMain(coroutineDispatcher)
    }

    @Test
    fun `test loading state`() = coroutineDispatcher.runBlockingTest {
        val result = viewModel.venuesState.first()

        assertEquals(result, VenuesUiState.Loading)
    }

    @Test
    fun `test successful empty venue loading`() = coroutineDispatcher.runBlockingTest {
        every { venueRepository.getVenues(any(), any(), any()) } returns flow {
            emit(Result.success<List<Venue>>(emptyList()))
        }

        viewModel.onMapCameraPositionUpdated(1.0, 1.0, 1)
        val result = viewModel.venuesState.first()

        coVerify { venueRepository.getVenues(any(), any(), any()) }
        assert(result is VenuesUiState.VenuesAvailable)
        assert((result as VenuesUiState.VenuesAvailable).venues.isEmpty())
    }

    @Test
    fun `test successful venue loading`() = coroutineDispatcher.runBlockingTest {
        val venue = mockk<Venue>()
        every { venueRepository.getVenues(any(), any(), any()) } returns flow {
            emit(Result.success<List<Venue>>(listOf(venue)))
        }

        viewModel.onMapCameraPositionUpdated(1.0, 1.0, 1)
        val result = viewModel.venuesState.first()

        coVerify { venueRepository.getVenues(any(), any(), any()) }
        assert(result is VenuesUiState.VenuesAvailable)
        assert((result as VenuesUiState.VenuesAvailable).venues.isNotEmpty())
        assert(result.venues[0] == venue)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}