package com.mehrbod.domain.usecase

import android.location.Location
import com.mehrbod.domain.repository.LocationRepository
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

import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserLocationUseCaseTest {

    @MockK
    lateinit var locationRepository: LocationRepository

    @InjectMockKs
    lateinit var getUserLocationUseCase: GetUserLocationUseCase

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test user location available`() = coroutineDispatcher.runBlockingTest {
        val location = mockk<Location>()
        coEvery { locationRepository.findUserLocation() } returns Result.success(location)

        val result = getUserLocationUseCase.getUserLocation()

        coVerify { locationRepository.findUserLocation() }
        assertEquals(result.getOrNull(), location)
    }

    @Test
    fun `test user location not available`() = coroutineDispatcher.runBlockingTest {
        val throwable = mockk<Throwable>(relaxed = true)
        coEvery { locationRepository.findUserLocation() } returns Result.failure(throwable)

        val result = getUserLocationUseCase.getUserLocation()

        coVerify { locationRepository.findUserLocation() }
        assertEquals(result.exceptionOrNull(), throwable)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}