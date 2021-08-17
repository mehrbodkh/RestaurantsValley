package com.mehrbod.restaurantsvalley.ui.restaurantsonmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.ItemVenueInfoBinding
import com.mehrbod.restaurantsvalley.ui.restaurantsonmap.views.RestaurantInfoViewHolder


object RestaurantsInfoDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }
}

class RestaurantsInfoAdapter(private val onItemClickListener: (Restaurant) -> Unit) :
    ListAdapter<Restaurant, RestaurantInfoViewHolder>(RestaurantsInfoDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantInfoViewHolder {
        val binding =
            ItemVenueInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantInfoViewHolder, position: Int) {
        holder.onBind(getItem(position), onItemClickListener)
    }
}
