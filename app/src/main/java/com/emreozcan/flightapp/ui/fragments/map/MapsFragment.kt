package com.emreozcan.flightapp.ui.fragments.map

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.emreozcan.flightapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment() {
    private val args by navArgs<MapsFragmentArgs>()

    private val callback = OnMapReadyCallback { googleMap ->

        val latitudeAndLongitude = args.latitudeAndLongitude
        val parse = latitudeAndLongitude.split(",").toTypedArray()

        val airport = LatLng(parse[0].toDouble(), parse[1].toDouble())
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        googleMap.addMarker(
            MarkerOptions().position(airport).title(args.airportName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    ).snippet(geocoder.getFromLocation(parse[0].toDouble(), parse[1].toDouble(),1).toString())
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airport, 15f))
        googleMap.addCircle(CircleOptions().apply {
            center(airport)
            radius(500.0)
            strokeColor(R.color.teal_700)
        })

        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}