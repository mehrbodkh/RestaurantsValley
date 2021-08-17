package com.mehrbod.restaurantsvalley.ui.venuedetails

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.repository.RestaurantsRepository
import com.mehrbod.domain.usecase.GetRestaurantDetailsUseCase
import com.mehrbod.restaurantsvalley.ui.venuedetails.states.RestaurantDetailUIState
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VenueDetailsViewModelTest {
    @RelaxedMockK
    lateinit var restaurantsRepository: RestaurantsRepository

    @InjectMockKs
    lateinit var getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase

    @InjectMockKs
    lateinit var viewModel: VenueDetailsViewModel

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test initial state`() = coroutineDispatcher.runBlockingTest {
        val state = viewModel.restaurantDetailUIState.first()

        assert(state is RestaurantDetailUIState.Loading)
    }

    @Test
    fun `test get restaurant details - failure`() = coroutineDispatcher.runBlockingTest {
        coEvery { getRestaurantDetailsUseCase.execute(any()) } returns flowOf(Result.failure(Throwable("")))

        viewModel.onRestaurantIdReceived("1")
        val state = viewModel.restaurantDetailUIState.first()

        coVerify { getRestaurantDetailsUseCase.execute("1") }
        assert(state is RestaurantDetailUIState.Failure)
        assert((state as RestaurantDetailUIState.Failure).message == "No restaurants found")
    }

    @Test
    fun `test get restaurant details - success`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        every { restaurant getProperty "id" } returns "1"
        every { restaurant getProperty "name" } returns "name"
        coEvery { getRestaurantDetailsUseCase.execute(any()) } returns flowOf(Result.success(restaurant))

        viewModel.onRestaurantIdReceived("1")
        val state = viewModel.restaurantDetailUIState.first()

        coVerify { getRestaurantDetailsUseCase.execute("1") }
        assert(state is RestaurantDetailUIState.RestaurantDetailAvailable)
        assert((state as RestaurantDetailUIState.RestaurantDetailAvailable).restaurant.name == "name")
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

}