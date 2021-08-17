package com.mehrbod.domain.usecase

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
class LocationPermissionGrantedInfoUseCaseTest {


    @MockK
    lateinit var locationRepository: LocationRepository

    @InjectMockKs
    lateinit var locationPermissionGrantedInfoUseCase: LocationPermissionGrantedInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test location permission - enabled`() {
        every { locationRepository.isLocationPermissionGranted() } returns true

        val result = locationPermissionGrantedInfoUseCase.isLocationPermissionGranted()

        verify { locationRepository.isLocationPermissionGranted() }
        assertTrue(result)
    }

    @Test
    fun `test location permission - disabled`() {
        every { locationRepository.isLocationPermissionGranted() } returns false

        val result = locationPermissionGrantedInfoUseCase.isLocationPermissionGranted()

        verify { locationRepository.isLocationPermissionGranted() }
        assertFalse(result)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}