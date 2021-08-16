package com.mehrbod.data.api.adapter

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.data.api.response.ApiVenuesResponse

fun ApiVenuesResponse.convertToRestaurants(): List<Restaurant> {
    return this.responseDto.venues.map {
        Restaurant(
            it.id,
            it.name,
            it.contactDto?.let { apiContact ->
                com.mehrbod.domain.model.restaurant.Contact(
                    apiContact.phone,
                    apiContact.formattedPhone
                )
            },
            com.mehrbod.domain.model.restaurant.Location(
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
                com.mehrbod.domain.model.restaurant.Categories(
                    category.id,
                    category.name,
                    category.pluralName,
                    category.shortName,
                    com.mehrbod.domain.model.restaurant.Icon(
                        category.iconDto.prefix,
                        category.iconDto.mapPrefix,
                        category.iconDto.suffix
                    ),
                    category.primary
                )
            },
            it.verified,
            it.statsDto?.let { apiStats ->
                com.mehrbod.domain.model.restaurant.Stats(
                    apiStats.tipCount,
                    apiStats.usersCount,
                    apiStats.checkinsCount
                )
            },
            it.specialsDto?.let { apiSpecials ->
                com.mehrbod.domain.model.restaurant.Specials(
                    apiSpecials.count,
                    apiSpecials.items
                )
            },
            it.venuePageDto?.let { apiVenuePage ->
                com.mehrbod.domain.model.restaurant.VenuePage(
                    apiVenuePage.id
                )
            },
            it.locked,
            it.hereNowDto?.let { hereNow ->
                com.mehrbod.domain.model.restaurant.HereNow(
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