package com.emreozcan.flightapp.ui.fragments.onboarding

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentVideoBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.youtube.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener


class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        var viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)

        binding.buttonContinueVideo?.setOnClickListener {
            if (viewPager == null ){
                viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            }
            viewPager?.currentItem =  1
        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.youtubePlayerView.enterFullScreen()
        }


        binding.youtubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                    activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                }
                activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            }

            override fun onYouTubePlayerExitFullScreen() {
                activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
        })

        binding.firstScreenFinish?.setOnClickListener {
            mainViewModel.saveOnboarding()
            val orientation = getResources().getConfiguration().orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}