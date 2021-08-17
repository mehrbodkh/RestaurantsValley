package com.mehrbod.restaurantsvalley.ui.restaurantsonmap.views

import androidx.recyclerview.widget.RecyclerView
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.ItemVenueInfoBinding


class RestaurantInfoViewHolder(private val binding: ItemVenueInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(restaurant: Restaurant, onItemClickListener: (Restaurant) -> Unit) {
        binding.name.text = restaurant.name
        binding.distance.text = String.format(
            binding.root.context.getString(R.string.distance_unit),
            restaurant.location.distance
        )
        binding.address.text = restaurant.location.address
        binding.root.setOnClickListener {
            onItemClickListener(restaurant)
        }
    }
}