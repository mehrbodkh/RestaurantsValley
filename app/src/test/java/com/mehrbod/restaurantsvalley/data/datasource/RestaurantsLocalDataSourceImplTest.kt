package com.mehrbod.restaurantsvalley.data.datasource

import com.mehrbod.restaurantsvalley.domain.model.Restaurant
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsLocalDataSourceImplTest {

    @MockK
    lateinit var restaurant: Restaurant

    private lateinit var localDataSource: RestaurantsLocalDataSource
    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localDataSource = RestaurantsLocalDataSourceImpl()
        coroutineDispatcher = TestCoroutineDispatcher()
    }

    @Test
    fun `test get restaurant details - failure`() = coroutineDispatcher.runBlockingTest {
        localDataSource.updateRestaurants(listOf())
        val result = localDataSource.getRestaurantDetail("1")

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Nothing has been found")
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
}