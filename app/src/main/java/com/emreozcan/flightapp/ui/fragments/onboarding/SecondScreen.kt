package com.emreozcan.flightapp.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSecondScreenBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel


class SecondScreen : Fragment() {


    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondScreenBinding.inflate(inflater,container,false)


        startAnimations()


        binding.buttonFinish.setOnClickListener {
            mainViewModel.saveOnboarding()
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }




        return binding.root
    }
    private fun startAnimations(){
        val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.anim_alpha)
        binding.imageView7.startAnimation(anim)
        binding.textView6.startAnimation(anim)
        binding.textView7.startAnimation(anim)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}