package com.mehrbod.restaurantsvalley.presentation.venueonmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.ItemVenueInfoBinding
import com.mehrbod.domain.model.restaurant.Restaurant


object VenuesDiffCallback : DiffUtil.ItemCallback<com.mehrbod.domain.model.restaurant.Restaurant>() {
    override fun areItemsTheSame(oldItem: com.mehrbod.domain.model.restaurant.Restaurant, newItem: com.mehrbod.domain.model.restaurant.Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: com.mehrbod.domain.model.restaurant.Restaurant, newItem: com.mehrbod.domain.model.restaurant.Restaurant): Boolean {
        return oldItem.id == newItem.id
    }
}

class VenuesInfoAdapter(private val onItemClickListener: (com.mehrbod.domain.model.restaurant.Restaurant) -> Unit) :
    ListAdapter<com.mehrbod.domain.model.restaurant.Restaurant, VenueInfoViewHolder>(VenuesDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueInfoViewHolder {
        val binding =
            ItemVenueInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VenueInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueInfoViewHolder, position: Int) {
        holder.onBind(getItem(position), onItemClickListener)
    }
}

class VenueInfoViewHolder(private val binding: ItemVenueInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(restaurant: com.mehrbod.domain.model.restaurant.Restaurant, onItemClickListener: (com.mehrbod.domain.model.restaurant.Restaurant) -> Unit) {
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