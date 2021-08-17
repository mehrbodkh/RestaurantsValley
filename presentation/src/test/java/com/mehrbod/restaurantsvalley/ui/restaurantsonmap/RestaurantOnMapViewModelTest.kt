package com.mehrbod.restaurantsvalley.ui.restaurantsonmap

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.mehrbod.domain.repository.RestaurantsRepository
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.usecase.GetRestaurantsUseCase
import com.mehrbod.domain.usecase.GetUserLocationUseCase
import com.mehrbod.domain.usecase.LocationEnabledInfoUseCase
import com.mehrbod.domain.usecase.LocationPermissionGrantedInfoUseCase
import com.mehrbod.restaurantsvalley.ui.restaurantsdetails.VenueDetailsViewModel
import com.mehrbod.restaurantsvalley.ui.restaurantsonmap.states.LocationUiState
import com.mehrbod.restaurantsvalley.ui.restaurantsonmap.states.RestaurantsUiState
import com.mehrbod.restaurantsvalley.data.repository.LocationRepositoryImpl
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantOnMapViewModelTest {

    @RelaxedMockK
    lateinit var restaurantsRepository: RestaurantsRepository

    @RelaxedMockK
    lateinit var locationRepositoryImpl: LocationRepositoryImpl

    @InjectMockKs
    lateinit var getRestaurantsUseCase: GetRestaurantsUseCase

    @InjectMockKs
    lateinit var locationUseCase: GetUserLocationUseCase

    @InjectMockKs
    lateinit var locationPermissionInfoUseCase: LocationPermissionGrantedInfoUseCase

    @InjectMockKs
    lateinit var locationEnabledUseCase: LocationEnabledInfoUseCase

    @InjectMockKs
    lateinit var viewModel: RestaurantsOnMapViewModel

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher


    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test loading state`() = coroutineDispatcher.runBlockingTest {
        val result = viewModel.restaurantsState.first()

        assertEquals(result, RestaurantsUiState.Loading)
    }

    @Test
    fun `test successful empty restaurant loading`() = coroutineDispatcher.runBlockingTest {
        every { getRestaurantsUseCase.execute(any(), any(), any()) } returns flow {
            emit(Result.success<List<Restaurant>>(emptyList()))
        }

        viewModel.onSearchAreaClicked(1.0, 1.0, 1)
        val result = viewModel.restaurantsState.first()

        coVerify { getRestaurantsUseCase.execute(1.0, 1.0, 1) }
        assert(result is RestaurantsUiState.RestaurantsAvailable)
        assert((result as RestaurantsUiState.RestaurantsAvailable).restaurants.isEmpty())
    }

    @Test
    fun `test restaurants loading - success`() = coroutineDispatcher.runBlockingTest {
        val venue = mockk<Restaurant>()
        every { getRestaurantsUseCase.execute(any(), any(), any()) } returns flow {
            emit(Result.success<List<Restaurant>>(listOf(venue)))
        }

        viewModel.onSearchAreaClicked(1.0, 1.0, 1)
        val result = viewModel.restaurantsState.first()

        coVerify { getRestaurantsUseCase.execute(1.0, 1.0, 1) }
        assert(result is RestaurantsUiState.RestaurantsAvailable)
        assert((result as RestaurantsUiState.RestaurantsAvailable).restaurants.isNotEmpty())
        assert(result.restaurants[0] == venue)
    }

    @Test
    fun `test restaurants loading - failure`() = coroutineDispatcher.runBlockingTest {
        every { getRestaurantsUseCase.execute(any(), any(), any()) } returns flow {
            emit(Result.failure<List<Restaurant>>(Throwable("12323")))
        }

        viewModel.onSearchAreaClicked(1.0, 1.0, 1)
        val result = viewModel.restaurantsState.first()

        coVerify { getRestaurantsUseCase.execute(1.0, 1.0, 1) }
        assert(result is RestaurantsUiState.Failure)
        assert((result as RestaurantsUiState.Failure).message == "12323")
    }

    @Test
    fun `test location permission granted`() = coroutineDispatcher.runBlockingTest {
        coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.success(true)
        every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns true
        coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

        viewModel.onPermissionResult(true)
        val result = viewModel.locationState.first()

        assert(result is LocationUiState.LocationAvailable)
    }

    @Test
    fun `test location request location - permission granted - gps on`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.success(true)
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns true
            coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationAvailable)
        }

    @Test
    fun `test location request location - permission denied - gps on`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.success(true)
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns false
            coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationPermissionNeeded)
        }

    @Test
    fun `test location request location - permission granted - gps off`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.failure(Throwable(""))
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns true
            coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.Failure)
        }

    @Test
    fun `test location request location - permission granted - gps off - ready to enable`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.failure(
                ResolvableApiException(
                    Status.RESULT_TIMEOUT
                )
            )
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns true
            coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.GPSNeeded)
        }

    @Test
    fun `test location request location - permission denied - gps off`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.failure(Throwable(""))
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns false
            coEvery { locationUseCase.getUserLocation() } returns Result.success(Location(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.LocationPermissionNeeded)
        }

    @Test
    fun `test location request location - location not found`() =
        coroutineDispatcher.runBlockingTest {
            coEvery { locationEnabledUseCase.isLocationEnabled() } returns Result.success(true)
            every { locationPermissionInfoUseCase.isLocationPermissionGranted() } returns true
            coEvery { locationUseCase.getUserLocation() } returns Result.failure(Throwable(""))

            viewModel.onRequestLocationClicked()
            val result = viewModel.locationState.first()

            assert(result is LocationUiState.Failure)
        }

    @Test
    fun `on user location showing`() = coroutineDispatcher.runBlockingTest {
        every { getRestaurantsUseCase.execute(any(), any(), any()) } returns flow {
            emit(Result.success<List<Restaurant>>(emptyList()))
        }

        viewModel.onUserLocationShowing(1.0, 1.0, 1)
        val result = viewModel.restaurantsState.first()

        coVerify { getRestaurantsUseCase.execute(1.0, 1.0, 1) }
        assert(result is RestaurantsUiState.RestaurantsAvailable)
        assert((result as RestaurantsUiState.RestaurantsAvailable).restaurants.isEmpty())
    }

    @Test
    fun `test click on restaurant`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        every { restaurant.id } returns "123"

        viewModel.onRestaurantClicked(restaurant)
        val result = viewModel.restaurantsState.first()

        assert(result is RestaurantsUiState.VenueDetailsAvailable)
        assert(
            (result as RestaurantsUiState.VenueDetailsAvailable).key == VenueDetailsViewModel.RESTAURANT_ID
        )
        assert(result.restaurantId == "123")
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}