package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehrbod.restaurantsvalley.databinding.ItemVenueInfoBinding
import com.mehrbod.restaurantsvalley.domain.model.Venue


object VenuesDiffCallback : DiffUtil.ItemCallback<Venue>() {
    override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem.id == newItem.id
    }
}

class VenuesInfoAdapter : ListAdapter<Venue, VenueInfoViewHolder>(VenuesDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueInfoViewHolder {
        val binding =
            ItemVenueInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VenueInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueInfoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class VenueInfoViewHolder(private val binding: ItemVenueInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(venue: Venue) {
        binding.name.text = venue.name
    }
}