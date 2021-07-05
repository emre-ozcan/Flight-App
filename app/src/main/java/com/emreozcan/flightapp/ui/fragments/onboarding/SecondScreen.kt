package com.emreozcan.flightapp.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {


    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondScreenBinding.inflate(inflater,container,false)

        binding.buttonFinish.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}