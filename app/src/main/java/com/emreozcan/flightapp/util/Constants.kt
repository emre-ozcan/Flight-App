package com.emreozcan.flightapp.util

import com.emreozcan.flightapp.BuildConfig

class Constants {
    companion object{
        // Firebase
        const val FIREBASE_COLLECTION = "airports"
        const val FIREBASE_COLLECTION_USER = "users"

        // Datastore
        const val DATASTORE_PREFERENCE_NAME = "flight_preferences"
        const val ONBOARDING_PREFERENCE_KEY = "onboarding"

        // Remote Config Force Update
        const val STORE_URL = "store_url"
        const val SUGGESTED_VERSION_CODE = "suggested_version_code"
        const val LEAST_VERSION_CODE = "lesat_version_code"
        const val FORCE_UPDATE_REQUIRED = "force_update_required"
        const val APP_VERSION_CODE = BuildConfig.VERSION_CODE

        // Yotube Player
        const val YOUTUBE_PLAYER_KEY = "AIzaSyBUC7zGDJIy32P--kkonTJqmrQ1eQfpCwU"
        const val YOUTUBE_VIDEO_KEY = "6esb5_f0aDs"

    }
}