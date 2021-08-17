package com.mehrbod.domain.usecase

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.repository.RestaurantsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {
    fun execute(lat: Double, lng: Double, radius: Int): Flow<Result<List<Restaurant>>> =
        repository.getRestaurants(lat, lng, radius)
}