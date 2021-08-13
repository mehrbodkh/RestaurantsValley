package com.mehrbod.restaurantsvalley.presentation.venueonmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mehrbod.restaurantsvalley.R
import com.mehrbod.restaurantsvalley.databinding.VenueOnMapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VenueOnMapFragment : Fragment() {

    private lateinit var viewModel: VenueOnMapViewModel

    private var _binding: VenueOnMapFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VenueOnMapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VenueOnMapViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}