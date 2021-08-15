package com.mehrbod.restaurantsvalley.presentation.venuedetails

import com.mehrbod.restaurantsvalley.data.repository.RestaurantsRepository
import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import com.mehrbod.restaurantsvalley.presentation.venuedetails.states.RestaurantDetailUIState
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VenueDetailsViewModelTest {

    @MockK
    lateinit var repository: RestaurantsRepository

    private lateinit var viewModel: VenueDetailsViewModel
    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = VenueDetailsViewModel(repository)
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
    }

    @Test
    fun `test initial state`() = coroutineDispatcher.runBlockingTest {
        val state = viewModel.restaurantDetailUIState.first()

        assert(state is RestaurantDetailUIState.Loading)
    }

    @Test
    fun `test get restaurant details - failure`() = coroutineDispatcher.runBlockingTest {
        coEvery { repository.getRestaurantDetails(any()) } returns flowOf(Result.failure(Throwable("")))

        viewModel.onRestaurantIdReceived("1")
        val state = viewModel.restaurantDetailUIState.first()

        assert(state is RestaurantDetailUIState.Failure)
        assert((state as RestaurantDetailUIState.Failure).message == "No restaurants found")
    }

    @Test
    fun `test get restaurant details - success`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        every { restaurant getProperty "id" } returns "1"
        every { restaurant getProperty "name" } returns "name"
        coEvery { repository.getRestaurantDetails(any()) } returns flowOf(Result.success(restaurant))

        viewModel.onRestaurantIdReceived("1")
        val state = viewModel.restaurantDetailUIState.first()

        assert(state is RestaurantDetailUIState.RestaurantDetailAvailable)
        assert((state as RestaurantDetailUIState.RestaurantDetailAvailable).restaurant.name == "name")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}