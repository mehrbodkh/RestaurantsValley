package com.mehrbod.domain.usecase

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.repository.RestaurantsRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRestaurantsUseCaseTest {

    @RelaxedMockK
    lateinit var restaurantsRepository: RestaurantsRepository

    @InjectMockKs
    lateinit var getRestaurantsUseCase: GetRestaurantsUseCase

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test get restaurants successful response - no cache`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        every {
            restaurantsRepository.getRestaurants(any(), any(), any())
        } returns flowOf(Result.success(listOf(restaurant)))

        val firstResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).first()
        val lastResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).last()

        coVerify { restaurantsRepository.getRestaurants(1.0, 1.0, 1) }
        assertTrue(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assertEquals(lastResponse, firstResponse)
    }

    @Test
    fun `test get restaurants successful response - cached`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        val restaurant2 = mockk<Restaurant>()
        every {
            restaurantsRepository.getRestaurants(any(), any(), any())
        } returns flowOf(Result.success(listOf(restaurant)), Result.success(listOf(restaurant2)))

        val firstResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).first()
        val lastResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).last()

        coVerify { restaurantsRepository.getRestaurants(1.0, 1.0, 1) }
        assertTrue(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assertTrue(lastResponse.isSuccess)
        assert(lastResponse.getOrNull() != null)
        assert(lastResponse.getOrNull()!!.isNotEmpty())
        assertNotEquals(lastResponse, firstResponse)
    }

    @Test
    fun `test get restaurants failed response - cached`() = coroutineDispatcher.runBlockingTest {
        val restaurant = mockk<Restaurant>()
        every {
            restaurantsRepository.getRestaurants(any(), any(), any())
        } returns flowOf(Result.success(listOf(restaurant)), Result.failure(Throwable("")))

        val firstResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).first()
        val lastResponse = getRestaurantsUseCase.execute(1.0, 1.0, 1).last()

        coVerify { restaurantsRepository.getRestaurants(1.0, 1.0, 1) }
        assertTrue(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assertTrue(lastResponse.isFailure)
    }

    @Test
    fun `test restaurant failed response - no cache`() = coroutineDispatcher.runBlockingTest {
        every {
            restaurantsRepository.getRestaurants(any(), any(), any())
        } returns flowOf(Result.failure(Throwable("")), Result.failure(Throwable("")))

        val response = getRestaurantsUseCase.execute(1.0, 1.0, 1).last()

        coVerify { restaurantsRepository.getRestaurants(1.0, 1.0, 1) }
        assert(response.isFailure)
        assertEquals(null, response.getOrNull())
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}