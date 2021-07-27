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
        const val STORE_URL_DEFAULT = "https://play.google.com/store/apps/details?id=com.nczsoftware.waterreminderapp&hl=tr&gl=US"
        const val SUGGESTED_VERSION_CODE = "suggested_version_code"
        const val SUGGESTED_VERSION_CODE_DEFAULT = 1
        const val LEAST_VERSION_CODE = "lesat_version_code"
        const val LEAST_VERSION_CODE_DEFAULT = 1
        const val FORCE_UPDATE_REQUIRED = "force_update_required"
        const val FORCE_UPDATE_REQUIRED_DEFAULT = false
        const val APP_VERSION_CODE = BuildConfig.VERSION_CODE

       // Firebase CM Push Notification
        const val BASE_URL = "https://fcm.googleapis.com"
        const val CONTENT_TYPE = "application/json"
        const val SERVER_KEY = "AAAADjdF4y4:APA91bEByWolN62BciwO4EhtGcw9YErU3bEIIbUHjhlahlvW-VNyt2bPJoPn2NglQ6BMZ1XVyyb1b_dyVl9jR7JKO3MmmlWND8MVw72gUby7n4DQVaQVWjRnKJzGbBUtOGIF7mxEig5U"
        const val AIRPORT_TOPIC = "/topics/airports"

    }
}