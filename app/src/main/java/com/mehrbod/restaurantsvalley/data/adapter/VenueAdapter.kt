package com.mehrbod.restaurantsvalley.data.adapter

import com.mehrbod.restaurantsvalley.data.api.model.ApiVenuesResponse
import com.mehrbod.restaurantsvalley.data.model.*

fun ApiVenuesResponse.convertToVenues(): List<Venue> {
    return this.apiResponse.venues.map {
        Venue(
            it.id,
            it.name,
            if (it.apiContact != null) Contact(
                it.apiContact.phone,
                it.apiContact.formattedPhone
            ) else null,
            if (it.apiLocation != null) Location(
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
            ) else null,
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
            if (it.apiStats != null) Stats(it.apiStats.tipCount, it.apiStats.usersCount, it.apiStats.checkinsCount) else null,
            if (it.apiSpecials != null) Specials(it.apiSpecials.count, it.apiSpecials.items) else null,
            if (it.apiVenuePage != null) VenuePage(it.apiVenuePage.id) else null,
            it.locked,
            it.apiHereNow?.let { hereNow -> HereNow(hereNow.count, hereNow.summary, hereNow.groups) },
            it.referralId,
            it.venueChains,
            it.hasPerk
        )
    }
}