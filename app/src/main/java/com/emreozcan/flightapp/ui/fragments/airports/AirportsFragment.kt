package com.emreozcan.flightapp.ui.fragments.airports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.flightapp.adapters.AirportsRowAdapter
import com.emreozcan.flightapp.databinding.FragmentAirportsBinding
import com.emreozcan.flightapp.util.setupRecyclerView
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
        _binding = FragmentAirportsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter

        mainViewModel.getData()


        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}