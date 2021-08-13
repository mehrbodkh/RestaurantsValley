package com.mehrbod.restaurantsvalley.data.adapter

import com.mehrbod.restaurantsvalley.data.api.model.ApiVenuesResponse
import com.mehrbod.restaurantsvalley.domain.model.*

fun ApiVenuesResponse.convertToVenues(): List<Venue> {
    return this.apiResponse.venues.map {
        Venue(
            it.id,
            it.name,
            it.apiContact?.let { apiContact ->
                Contact(
                    apiContact.phone,
                    apiContact.formattedPhone
                )
            },
            Location(
                it.apiLocation.address,
                it.apiLocation.crossStreet,
                it.apiLocation.lat,
                it.apiLocation.lng,
                it.apiLocation.distance,
                it.apiLocation.postalCode,
                it.apiLocation.cc,
                it.apiLocation.neighborhood,
                it.apiLocation.city,
                it.apiLocation.state,
                it.apiLocation.country,
                it.apiLocation.contextLine,
                it.apiLocation.contextGeoId,
                it.apiLocation.formattedAddress
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
                        category.apiIcon.prefix,
                        category.apiIcon.mapPrefix,
                        category.apiIcon.suffix
                    ),
                    category.primary
                )
            },
            it.verified,
            it.apiStats?.let { apiStats ->
                Stats(
                    apiStats.tipCount,
                    apiStats.usersCount,
                    apiStats.checkinsCount
                )
            },
            it.apiSpecials?.let { apiSpecials ->
                Specials(
                    apiSpecials.count,
                    apiSpecials.items
                )
            },
            it.apiVenuePage?.let { apiVenuePage -> VenuePage(apiVenuePage.id) },
            it.locked,
            it.apiHereNow?.let { hereNow ->
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