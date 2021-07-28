package com.emreozcan.flightapp.ui

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.ActivityLoginBinding
import com.emreozcan.flightapp.services.NotificationService
import com.emreozcan.flightapp.ui.fragments.onboarding.FirstScreen
import com.emreozcan.flightapp.ui.fragments.onboarding.VideoFragment
import com.emreozcan.flightapp.util.Constants.Companion.AIRPORT_TOPIC
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle().apply {
            this.putString("application","start")
        }
        firebaseAnalytics.logEvent("LoginActivity_OnCreate",bundle)

        FirebaseMessaging.getInstance().subscribeToTopic(AIRPORT_TOPIC)

        createDailyNotification()


    }

    private fun createDailyNotification() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,10)
        calendar.set(Calendar.MINUTE,20)
        calendar.set(Calendar.SECOND,0)

        if (calendar.time < Date()){
            calendar.add(Calendar.DAY_OF_MONTH,1)
        }

        val intent = Intent(applicationContext,NotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)

    }

    override fun onDestroy() {
        super.onDestroy()
        val bundle = Bundle().apply {
            this.putString("application","finish")
        }
        firebaseAnalytics.logEvent("LoginActivity_OnDestroy",bundle)
    }


    override fun onBackPressed() {
        val bundle = Bundle().apply {
            this.putString("application","backpressed")
        }
        firebaseAnalytics.logEvent("LoginActivity_OnBackPressed",bundle)

        val orientation = getResources().getConfiguration().orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        } else {
            super.onBackPressed()
        }
    }
}