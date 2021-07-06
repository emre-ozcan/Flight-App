package com.emreozcan.flightapp.ui.fragments.flights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.adapters.AirportsRowAdapter
import com.emreozcan.flightapp.adapters.FlightsRowAdapter
import com.emreozcan.flightapp.databinding.FragmentFlightsBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel


class FlightsFragment : Fragment() {

    private var _binding : FragmentFlightsBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter by lazy { FlightsRowAdapter() }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlightsBinding.inflate(inflater,container,false)


        setupRecycler()
        mainViewModel.getData()
        mainViewModel.airportsList.observe(viewLifecycleOwner,{ list ->
            mAdapter.setData(list)
            binding.recyclerViewFlights.scheduleLayoutAnimation()
        })




        return binding.root
    }
    private fun setupRecycler(){
        binding.recyclerViewFlights.adapter = mAdapter
        binding.recyclerViewFlights.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}