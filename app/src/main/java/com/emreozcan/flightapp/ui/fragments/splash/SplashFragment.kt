package com.emreozcan.flightapp.ui.fragments.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSplashBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding?= null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private var isOnboardingShowed = false

    var isHasInternetConnection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isHasInternetConnection = mainViewModel.hasInternetConnection()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)

        startAnimations()

        mainViewModel.readOnboarding.observe(viewLifecycleOwner,{
            isOnboardingShowed = it
        })


        if (isHasInternetConnection){
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (mainViewModel.currentUser != null){
                    findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                }else if(isOnboardingShowed){
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                }

            },2000)
        }else{
            MaterialAlertDialogBuilder(requireContext()).setTitle("İnternet Bağlantısı Yok")
                .setMessage("Uygulamanın çalışabilmesi için internet bağlantısına ihtiyaç vardır !")
                .setPositiveButton("Tamam"){ dialog, which ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }.setCancelable(false).show()
        }
        return binding.root
    }

    private fun startAnimations() {
        val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.plane_anim)
        binding.imageView.startAnimation(anim)

        val animText = AnimationUtils.loadAnimation(requireContext(),R.anim.anim_alpha)
        binding.textView.startAnimation(animText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}