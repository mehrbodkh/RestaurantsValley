package com.mehrbod.domain.usecase

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.repository.RestaurantsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
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
class GetRestaurantDetailsUseCaseTest {
    @RelaxedMockK
    lateinit var restaurant: Restaurant

    @RelaxedMockK
    lateinit var restaurantsRepository: RestaurantsRepository

    @InjectMockKs
    lateinit var getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher


    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test details use case - success`() = coroutineDispatcher.runBlockingTest {
        coEvery { restaurantsRepository.getRestaurantDetails(any()) } returns flowOf(
            Result.success(
                restaurant
            )
        )

        val result = getRestaurantDetailsUseCase.execute("1").first()

        coVerify { restaurantsRepository.getRestaurantDetails("1") }
        assert(result.isSuccess)
        assert(result.getOrNull() == restaurant)
    }

    @Test
    fun `test details use case - failure`() = coroutineDispatcher.runBlockingTest {
        coEvery { restaurantsRepository.getRestaurantDetails(any()) } returns flowOf(
            Result.failure(Throwable("nothing"))
        )

        val result = getRestaurantDetailsUseCase.execute("1").first()

        coVerify { restaurantsRepository.getRestaurantDetails("1") }
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "nothing")
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}