package com.mehrbod.domain.usecase

import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.domain.repository.RestaurantsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetRestaurantDetailsUseCase @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository
) {

    fun execute(restaurantId: String): Flow<Result<Restaurant>> =
        restaurantsRepository.getRestaurantDetails(restaurantId)
}