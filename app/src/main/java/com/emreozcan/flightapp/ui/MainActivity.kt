package com.emreozcan.flightapp.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.ActivityMainBinding
import com.emreozcan.flightapp.ui.fragments.airportflights.AirportFlightsFragment
import com.emreozcan.flightapp.ui.fragments.airports.AirportsFragment
import com.emreozcan.flightapp.ui.fragments.profile.ProfileFragment
import com.emreozcan.flightapp.util.LocaleHelper
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        val navController = findNavController(R.id.navControllerMain)
        binding.bottomNavigationView.setupWithNavController(navController)

        println(getForegroundFragment())

        binding.fab.setOnClickListener {
            binding.bottomNavigationView.selectedItemId = R.id.placeholder
            when (getForegroundFragment()) {
                is ProfileFragment -> {
                    navController.navigate(R.id.action_action_profile_to_flightsFragment)
                }
                is AirportsFragment -> {
                    navController.navigate(R.id.action_action_airports_to_flightsFragment)
                }
                is AirportFlightsFragment -> {
                    navController.navigate(R.id.action_airportFlightsFragment_to_flightsFragment)
                }
            }

        }
        val bundle = Bundle().apply {
            this.putString("mainActivity","start")
        }
        firebaseAnalytics.logEvent("MainActivity_OnCreate",bundle)

    }
    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navControllerMain)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        val bundle = Bundle().apply {
            this.putString("mainActivity","finish")
        }
        firebaseAnalytics.logEvent("MainActivity_OnDestroy",bundle)
    }

    override fun onBackPressed() {
        val bundle = Bundle().apply {
            this.putString("mainActivity","onBackPressed")
        }
        firebaseAnalytics.logEvent("MainActivity_OnBackPressed",bundle)

        if (getForegroundFragment() is AirportsFragment){
            MaterialAlertDialogBuilder(this).setMessage(getString(R.string.close_application))
                .setPositiveButton(R.string.alert_yes){ dialog, which ->
                    super.onBackPressed()
                }.setNegativeButton(R.string.alert_no){ dialog, which ->
                }.show()
        }else{
            super.onBackPressed()
        }

    }
}