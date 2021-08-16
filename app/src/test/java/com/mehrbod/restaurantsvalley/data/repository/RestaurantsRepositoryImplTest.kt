package com.mehrbod.restaurantsvalley.data.repository

import com.mehrbod.data.repository.RestaurantsRepositoryImpl
import com.mehrbod.data.datasource.RestaurantsLocalDataSourceImpl
import com.mehrbod.data.datasource.RestaurantsRemoteDataSourceImpl
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsRepositoryImplTest {

    @RelaxedMockK
    lateinit var remoteDataSourceImpl: RestaurantsRemoteDataSourceImpl

    @RelaxedMockK
    lateinit var localDataSourceImpl: RestaurantsLocalDataSourceImpl

    @RelaxedMockK
    lateinit var restaurants: List<com.mehrbod.domain.model.restaurant.Restaurant>

    @RelaxedMockK
    lateinit var restaurant: com.mehrbod.domain.model.restaurant.Restaurant

    @InjectMockKs
    lateinit var restaurantsRepositoryImpl: RestaurantsRepositoryImpl

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test get restaurants success - method call`() = coroutineDispatcher.runBlockingTest {
        coEvery {
            localDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.failure(Throwable(""))

        coEvery {
            remoteDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.success(restaurants)

        restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).last()

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.updateRestaurants(any()) }
    }

    @Test
    fun `test get restaurants successful response - no cache`() = coroutineDispatcher.runBlockingTest {
        coEvery {
            localDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.failure(Throwable(""))

        coEvery {
            remoteDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.success(restaurants)

        val firstResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()
        val lastResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).last()

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.updateRestaurants(any()) }
        assert(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assert(lastResponse == firstResponse)
    }

    @Test
    fun `test get restaurants successful response - cached`() = coroutineDispatcher.runBlockingTest {
        coEvery {
            localDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.success(listOf(mockk()))

        coEvery {
            remoteDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.success(restaurants)

        val firstResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()
        val lastResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).last()

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.updateRestaurants(any()) }
        assert(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assert(lastResponse.isSuccess)
        assert(lastResponse.getOrNull() != null)
        assert(lastResponse.getOrNull()!!.isNotEmpty())
        assert(lastResponse != firstResponse)
    }

    @Test
    fun `test get restaurants failed response - cached`() = coroutineDispatcher.runBlockingTest {
        coEvery {
            localDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.success(restaurants)

        coEvery {
            remoteDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.failure(Throwable(""))

        val firstResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).first()
        val lastResponse = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).last()

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        assert(firstResponse.isSuccess)
        assert(firstResponse.getOrNull() != null)
        assert(firstResponse.getOrNull()!!.isNotEmpty())
        assert(lastResponse.isFailure)
    }

    @Test
    fun `test restaurant failed response - no cache`() = coroutineDispatcher.runBlockingTest {
        coEvery {
            localDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.failure(Throwable(""))

        coEvery {
            remoteDataSourceImpl.fetchRestaurants(any(), any(), any())
        } returns Result.failure(Throwable(""))

        val response = restaurantsRepositoryImpl.getRestaurants(1.0, 1.0, 1).last()

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        coVerify { localDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }

        coVerify { remoteDataSourceImpl.fetchRestaurants(1.0, 1.0, 1) }
        assert(response.isFailure)
        assert(response.getOrNull() == null)
    }

    @Test
    fun `test restaurant details - call`() = coroutineDispatcher.runBlockingTest {
        coEvery { localDataSourceImpl.getRestaurantDetail(any()) } returns Result.success(restaurant)

        restaurantsRepositoryImpl.getRestaurantDetails("1").first()

        coVerify { localDataSourceImpl.getRestaurantDetail("1") }
    }

    @Test
    fun `test restaurant details - success`() = coroutineDispatcher.runBlockingTest {
        coEvery { localDataSourceImpl.getRestaurantDetail(any()) } returns Result.success(restaurant)

        val result = restaurantsRepositoryImpl.getRestaurantDetails("1").first()

        assert(result.isSuccess)
        assert(result.getOrNull() == restaurant)
    }

    @Test
    fun `test restaurant details - failure`() = coroutineDispatcher.runBlockingTest {
        coEvery { localDataSourceImpl.getRestaurantDetail(any()) } returns Result.failure(Throwable(""))

        val result = restaurantsRepositoryImpl.getRestaurantDetails("1").first()

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "")
    }

    @Test
    fun `test restaurant details - exception`() = coroutineDispatcher.runBlockingTest {
        coEvery { localDataSourceImpl.getRestaurantDetail(any()) } throws Exception("Message")

        val result = restaurantsRepositoryImpl.getRestaurantDetails("1").first()

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Message")
    }

    @After
    fun finalize() {
        unmockkAll()
        Dispatchers.resetMain()
    }


}