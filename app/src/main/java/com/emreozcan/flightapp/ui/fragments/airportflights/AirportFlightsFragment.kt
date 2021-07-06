package com.emreozcan.flightapp.ui.fragments.airportflights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.adapters.AirportsFlightsRowAdapter
import com.emreozcan.flightapp.adapters.AirportsRowAdapter
import com.emreozcan.flightapp.databinding.FragmentAirportFlightsBinding
import com.emreozcan.flightapp.ui.fragments.map.MapsFragmentArgs

class AirportFlightsFragment : Fragment() {

    private val args by navArgs<AirportFlightsFragmentArgs>()


    private var _binding: FragmentAirportFlightsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy { AirportsFlightsRowAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentAirportFlightsBinding.inflate(inflater,container,false)

        setupRecyclerView()
        mAdapter.setData(args.flightList.toList()).apply {
            binding.recyclerFlightsAirport.scheduleLayoutAnimation()
        }


        return binding.root
    }
    private fun setupRecyclerView(){
        binding.recyclerFlightsAirport.adapter = mAdapter
        binding.recyclerFlightsAirport.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}