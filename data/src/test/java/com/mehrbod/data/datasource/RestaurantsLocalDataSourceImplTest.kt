package com.mehrbod.data.datasource

import com.mehrbod.data.util.cacheDataNotFound
import com.mehrbod.data.util.noDetailsFound
import com.mehrbod.data.util.distance
import com.mehrbod.domain.model.restaurant.Location
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsLocalDataSourceImplTest {

    @MockK
    lateinit var restaurant: com.mehrbod.domain.model.restaurant.Restaurant

    @InjectMockKs
    lateinit var localDataSource: RestaurantsLocalDataSource

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        localDataSource = RestaurantsLocalDataSourceImpl()
        MockKAnnotations.init(this)
        mockkStatic("com.mehrbod.data.util.LocationExtensions")
    }

    @Test
    fun `test get restaurant details - failure`() = coroutineDispatcher.runBlockingTest {
        val result = localDataSource.getRestaurantDetail("1")

        assert(result.isFailure)
        assert(result.exceptionOrNull() == noDetailsFound)
    }

    @Test
    fun `test get restaurant details - success`() = coroutineDispatcher.runBlockingTest {
        every { restaurant getProperty "id" } returns "1"
        every { restaurant getProperty "name" } returns "name"
        localDataSource.updateRestaurants(listOf(restaurant))
        val result = localDataSource.getRestaurantDetail("1")

        assert(result.isSuccess)
        assert(result.getOrNull()?.name == "name")
    }

    @Test
    fun `test fetch cached restaurants - failure - empty cache`() = coroutineDispatcher.runBlockingTest {
        val result = localDataSource.fetchRestaurants(1.0, 1.0, 1)

        assert(result.isFailure)
        assert(result.exceptionOrNull() == cacheDataNotFound)
    }

    @Test
    fun `test fetch cached restaurants - failure - not in distance`() = coroutineDispatcher.runBlockingTest {
        val location = mockk<Location>()
        every { location getProperty "lat" } returns 1.0
        every { location getProperty "lng" } returns 1.0
        every { any<Location>().distance(any(), any()) } returns 159
        every { restaurant getProperty "location" } returns location

        localDataSource.updateRestaurants(listOf(restaurant))
        val result = localDataSource.fetchRestaurants(35.0, 35.0, 1)

        assert(result.isFailure)
        assert(result.exceptionOrNull() == cacheDataNotFound)
    }

    @Test
    fun `test fetch cached restaurants - success`() = coroutineDispatcher.runBlockingTest {
        val location = mockk<Location>()
        every { location getProperty "lat" } returns 1.0
        every { location getProperty "lng" } returns 1.0
        every { any<Location>().distance(any(), any()) } returns 1
        every { restaurant getProperty "location" } returns location

        localDataSource.updateRestaurants(listOf(restaurant))
        val result = localDataSource.fetchRestaurants(1.0, 1.0, 1)

        assert(result.isSuccess)
        assert(result.getOrNull()?.get(0) == restaurant)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}