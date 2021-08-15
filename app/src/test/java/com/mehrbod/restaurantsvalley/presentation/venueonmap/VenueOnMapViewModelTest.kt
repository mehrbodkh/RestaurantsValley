package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.mehrbod.restaurantsvalley.data.repository.VenueRepository
import com.mehrbod.restaurantsvalley.domain.model.Venue
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.presentation.venueonmap.states.VenuesUiState
import com.mehrbod.restaurantsvalley.util.LocationHelper
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

    @RelaxedMockK
    lateinit var locationHelper: LocationHelper

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: VenueOnMapViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        viewModel = VenueOnMapViewModel(venueRepository, locationHelper)
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

        viewModel.onSearchAreaClicked(1.0, 1.0, 1)
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

        viewModel.onSearchAreaClicked(1.0, 1.0, 1)
        val result = viewModel.venuesState.first()

        coVerify { venueRepository.getVenues(any(), any(), any()) }
        assert(result is VenuesUiState.VenuesAvailable)
        assert((result as VenuesUiState.VenuesAvailable).venues.isNotEmpty())
        assert(result.venues[0] == venue)
    }

    @Test
    fun `test location permission granted`() = coroutineDispatcher.runBlockingTest {
        coEvery { locationHelper.isLocationEnabled() } returns Result.success(true)
        every { locationHelper.isLocationPermissionGranted() } returns true
        coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

        viewModel.onPermissionResult(true)
        val result = viewModel.locationState.first()

        assert(result is LocationUiState.LocationAvailable)
    }

    @Test
    fun `test location request location - permission granted - gps on`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.success(true)
            every { locationHelper.isLocationPermissionGranted() } returns true
            coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationAvailable)
        }

    @Test
    fun `test location request location - permission denied - gps on`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.success(true)
            every { locationHelper.isLocationPermissionGranted() } returns false
            coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationPermissionNeeded)
        }

    @Test
    fun `test location request location - permission granted - gps off`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.failure(Throwable(""))
            every { locationHelper.isLocationPermissionGranted() } returns true
            coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.Failure)
        }

    @Test
    fun `test location request location - permission granted - gps off - ready to enable`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.failure(
                ResolvableApiException(
                    Status.RESULT_TIMEOUT
                )
            )
            every { locationHelper.isLocationPermissionGranted() } returns true
            coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.GPSNeeded)
        }

    @Test
    fun `test location request location - permission denied - gps off`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.failure(Throwable(""))
            every { locationHelper.isLocationPermissionGranted() } returns false
            coEvery { locationHelper.findUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationPermissionNeeded)
        }

    @Test
    fun `test location request location - location not found`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationHelper.isLocationEnabled() } returns Result.success(true)
            every { locationHelper.isLocationPermissionGranted() } returns true
            coEvery { locationHelper.findUserLocation() } returns Result.failure(Throwable(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.Failure)
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}