package com.mehrbod.restaurantsvalley.ui.restaurantsdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mehrbod.domain.model.restaurant.Restaurant
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.RestaurantDetailsFragmentBinding
import com.mehrbod.restaurantsvalley.ui.restaurantsdetails.states.RestaurantDetailUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantDetailsDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: RestaurantDetailsViewModel

    private var _binding: RestaurantDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RestaurantDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantDetailsViewModel::class.java)

        handleArguments()
        initializeUIStateObserver()
    }

    private fun handleArguments() {
        arguments?.getString(RestaurantDetailsViewModel.RESTAURANT_ID)?.let {
            viewModel.onRestaurantIdReceived(it)
        }
    }

    private fun initializeUIStateObserver() {
        lifecycleScope.launch {
            viewModel.restaurantDetailUIState.collect {
                when (it) {
                    RestaurantDetailUIState.Loading -> showLoading()
                    is RestaurantDetailUIState.RestaurantDetailAvailable -> showRestaurantDetails(it.restaurant)
                    is RestaurantDetailUIState.Failure -> showFailure(it.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showRestaurantDetails(restaurant: Restaurant) = with(binding) {
        hideLoading()
        name.text = restaurant.name
        distance.text = String.format(
            binding.root.context.getString(R.string.distance_unit),
            restaurant.location.distance
        )
        type.text = restaurant.categories.joinToString(separator = ", ") { it.name }
        address.text = restaurant.location.formattedAddress?.joinToString(separator = " - ")
        contact.text =
            restaurant.contact?.formattedPhone ?: getString(R.string.phone_number_not_found)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}