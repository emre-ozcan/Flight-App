package com.emreozcan.flightapp.ui.fragments.flighthistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.flightapp.adapters.AirportsFlightsRowAdapter
import com.emreozcan.flightapp.databinding.FragmentFlightHistoryBinding
import com.emreozcan.flightapp.ui.fragments.airportflights.AirportFlightsFragmentArgs
import com.emreozcan.flightapp.util.setupRecyclerView


class FlightHistoryFragment : Fragment() {

    private var _binding : FragmentFlightHistoryBinding? = null
    private val binding get() = _binding!!

    private val args: FlightHistoryFragmentArgs by navArgs()

    private val mAdapter by lazy { AirportsFlightsRowAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightHistoryBinding.inflate(inflater,container,false)

        binding.flightHistoryRecyclerView.setupRecyclerView(mAdapter,LinearLayoutManager(requireContext()))


        mAdapter.setData(args.flightList.toList()).apply {
            binding.flightHistoryRecyclerView.scheduleLayoutAnimation()
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}