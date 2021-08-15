package com.mehrbod.restaurantsvalley.data.api.adapter

import com.mehrbod.restaurantsvalley.data.api.response.ApiVenuesResponse
import com.mehrbod.restaurantsvalley.domain.model.*

fun ApiVenuesResponse.convertToRestaurants(): List<Restaurant> {
    return this.responseDto.venues.map {
        Restaurant(
            it.id,
            it.name,
            it.contactDto?.let { apiContact ->
                Contact(
                    apiContact.phone,
                    apiContact.formattedPhone
                )
            },
            Location(
                it.locationDto.address,
                it.locationDto.crossStreet,
                it.locationDto.lat,
                it.locationDto.lng,
                it.locationDto.distance,
                it.locationDto.postalCode,
                it.locationDto.cc,
                it.locationDto.neighborhood,
                it.locationDto.city,
                it.locationDto.state,
                it.locationDto.country,
                it.locationDto.contextLine,
                it.locationDto.contextGeoId,
                it.locationDto.formattedAddress
            ),
            it.canonicalUrl,
            it.canonicalPath,
            it.categories.map { category ->
                Categories(
                    category.id,
                    category.name,
                    category.pluralName,
                    category.shortName,
                    Icon(
                        category.iconDto.prefix,
                        category.iconDto.mapPrefix,
                        category.iconDto.suffix
                    ),
                    category.primary
                )
            },
            it.verified,
            it.statsDto?.let { apiStats ->
                Stats(
                    apiStats.tipCount,
                    apiStats.usersCount,
                    apiStats.checkinsCount
                )
            },
            it.specialsDto?.let { apiSpecials ->
                Specials(
                    apiSpecials.count,
                    apiSpecials.items
                )
            },
            it.venuePageDto?.let { apiVenuePage -> VenuePage(apiVenuePage.id) },
            it.locked,
            it.hereNowDto?.let { hereNow ->
                HereNow(
                    hereNow.count,
                    hereNow.summary,
                    hereNow.groups
                )
            },
            it.referralId,
            it.venueChains,
            it.hasPerk
        )
    }
}