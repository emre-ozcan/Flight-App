package com.emreozcan.flightapp.ui.fragments.airports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.adapters.AirportsRowAdapter
import com.emreozcan.flightapp.databinding.FragmentAirportsBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel


class AirportsFragment : Fragment() {

    private var _binding: FragmentAirportsBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter by lazy { AirportsRowAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAirportsBinding.inflate(inflater,container,false)

        setupRecyclerView()

        mainViewModel.getData()

        mainViewModel.airportsList.observe(viewLifecycleOwner,{ list->
            mAdapter.setData(list)
            binding.recyclerViewAirports.scheduleLayoutAnimation()
        })


        mainViewModel.isLoading.observe(viewLifecycleOwner,{ loading ->
            when {
                loading -> {
                    showShimmer()
                }
                else -> {
                    hideShimmer()
                }
            }
        })
        return binding.root
    }

    private fun showShimmer(){
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.isVisible = true
        binding.recyclerViewAirports.isVisible = false
    }

    private fun hideShimmer(){
        binding.shimmerLayout.hideShimmer()
        binding.shimmerLayout.isVisible = false
        binding.recyclerViewAirports.isVisible = true
    }

    private fun setupRecyclerView(){
        binding.recyclerViewAirports.adapter = mAdapter
        binding.recyclerViewAirports.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}