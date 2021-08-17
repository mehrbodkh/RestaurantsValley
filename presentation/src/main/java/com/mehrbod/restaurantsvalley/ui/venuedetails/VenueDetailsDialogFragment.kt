package com.mehrbod.restaurantsvalley.ui.venuedetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.VenueDetailsFragmentBinding
import com.mehrbod.restaurantsvalley.ui.venuedetails.states.RestaurantDetailUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VenueDetailsDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: VenueDetailsViewModel

    private var _binding: VenueDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VenueDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VenueDetailsViewModel::class.java)

        handleArguments()
        initializeUIStateObserver()
    }

    private fun handleArguments() {
        arguments?.getString(VenueDetailsViewModel.RESTAURANT_ID)?.let {
            viewModel.onRestaurantIdReceived(it)
        }
    }

    private fun initializeUIStateObserver() {
        lifecycleScope.launch {
            viewModel.restaurantDetailUIState.collect {
                when (it) {
                    RestaurantDetailUIState.Loading -> showLoading()
                    is RestaurantDetailUIState.RestaurantDetailAvailable -> showRestaurantDetails(it.restaurant)
                    is RestaurantDetailUIState.Failure -> showFailure()
                }
            }
        }
    }

    private fun showLoading() {

    }

    private fun showFailure() {

    }

    private fun showRestaurantDetails(restaurant: Restaurant) = with(binding){
        name.text = restaurant.name
        distance.text = String.format(
            binding.root.context.getString(R.string.distance_unit),
            restaurant.location.distance
        )
        type.text = restaurant.categories.joinToString(separator = ", ") { it.name }
        address.text = restaurant.location.formattedAddress?.joinToString(separator = " - ")
        contact.text = restaurant.contact?.formattedPhone ?: "No phone numbers available"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}