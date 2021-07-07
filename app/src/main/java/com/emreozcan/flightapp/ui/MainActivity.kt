package com.emreozcan.flightapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.ActivityMainBinding
import com.emreozcan.flightapp.ui.fragments.airportflights.AirportFlightsFragment
import com.emreozcan.flightapp.ui.fragments.airports.AirportsFragment
import com.emreozcan.flightapp.ui.fragments.profile.ProfileFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        val navController = findNavController(R.id.navControllerMain)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            binding.bottomNavigationView.selectedItemId = R.id.placeholder
            when(getForegroundFragment()){
                is ProfileFragment ->{
                    navController.navigate(R.id.action_action_profile_to_flightsFragment)
               }
                is AirportsFragment -> {navController.navigate(R.id.action_action_airports_to_flightsFragment)}
                is AirportFlightsFragment -> {navController.navigate(R.id.action_airportFlightsFragment_to_flightsFragment)}
            }

        }

    }
    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navControllerMain)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }
}