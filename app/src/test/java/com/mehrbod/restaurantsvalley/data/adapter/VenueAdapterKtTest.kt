package com.mehrbod.restaurantsvalley.data.adapter

import com.mehrbod.restaurantsvalley.data.api.model.*
import com.mehrbod.restaurantsvalley.data.api.response.ApiVenuesResponse
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VenueAdapterKtTest {

    @RelaxedMockK
    lateinit var locationDto: LocationDto

    private lateinit var model: ApiVenuesResponse

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        model = ApiVenuesResponse(
            MetaDto(code = 200, errorDetail = null, errorType = null, requestId = "hjlkfjfs02834"),
            listOf(),
            ResponseDto(
                listOf(
                    VenuesDto(
                        "1",
                        name = "2",
                        locationDto = locationDto,
                        categories = emptyList(),
                    )
                ),
                confident = false
            )
        )
    }

    @Test
    fun `test response model to model conversion`() {
        val venues = model.convertToVenues()
        assertEquals(venues[0].id, "1")
        assertEquals(venues[0].name, "2")
        assertTrue(venues[0].categories.isEmpty())
    }
}