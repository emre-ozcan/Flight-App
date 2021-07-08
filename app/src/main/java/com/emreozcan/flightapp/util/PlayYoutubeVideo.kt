package com.emreozcan.flightapp.util

import android.widget.Toast
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentVideoBinding
import com.emreozcan.flightapp.ui.fragments.onboarding.VideoFragment
import com.emreozcan.flightapp.util.Constants.Companion.YOUTUBE_PLAYER_KEY
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class PlayYoutubeVideo(val videoId : String) : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener{


    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {

        p1?.loadVideo(videoId)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {

    }
}