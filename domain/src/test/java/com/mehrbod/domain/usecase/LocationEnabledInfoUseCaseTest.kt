package com.mehrbod.domain.usecase

import com.mehrbod.domain.repository.LocationRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationEnabledInfoUseCaseTest {

    @MockK
    lateinit var locationRepository: LocationRepository

    @InjectMockKs
    lateinit var locationEnabledInfoUseCase: LocationEnabledInfoUseCase

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun `test location enabled - enabled`() = coroutineDispatcher.runBlockingTest {
        coEvery { locationRepository.isLocationEnabled() } returns Result.success(true)

        val result = locationEnabledInfoUseCase.isLocationEnabled()

        coVerify { locationRepository.isLocationEnabled() }
        assertNotNull(result.getOrNull())
        assertTrue(result.getOrNull()!!)
    }

    @Test
    fun `test location enabled - not enabled`() = coroutineDispatcher.runBlockingTest {
        coEvery { locationRepository.isLocationEnabled() } returns Result.failure(Throwable("API"))

        val result = locationEnabledInfoUseCase.isLocationEnabled()

        coVerify { locationRepository.isLocationEnabled() }
        assertNotNull(result.exceptionOrNull())
        assertEquals(result.exceptionOrNull()?.message, "API")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}