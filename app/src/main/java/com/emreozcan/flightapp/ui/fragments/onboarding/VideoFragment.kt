package com.emreozcan.flightapp.ui.fragments.onboarding

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentVideoBinding
import com.emreozcan.flightapp.util.Constants.Companion.YOUTUBE_PLAYER_KEY
import com.emreozcan.flightapp.util.Constants.Companion.YOUTUBE_VIDEO_KEY
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayer.Provider
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt


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

        val youTubePlayerFragment = YouTubePlayerSupportFragmentXKt.newInstance()

        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.youtubeLayout, youTubePlayerFragment).commit()

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.buttonContinueVideo.setOnClickListener {
            viewPager?.currentItem = 1
        }

        youTubePlayerFragment.initialize(YOUTUBE_PLAYER_KEY, object : OnInitializedListener {
            override fun onInitializationSuccess(p0: Provider?, p1: YouTubePlayer?, p2: Boolean) {
                p1?.loadVideo(YOUTUBE_VIDEO_KEY)
                var check = false
                p1?.setOnFullscreenListener {
                    if (check) {
                        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        check = false
                    } else {

                        check = true
                    }
                }
                p1?.play()
            }

            override fun onInitializationFailure(p0: Provider?, p1: YouTubeInitializationResult?) {
                val errorMessage = p1.toString()
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                Log.d("errorMessage:", errorMessage)
            }

        })

        binding.firstScreenFinish.setOnClickListener {
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