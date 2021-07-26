package com.emreozcan.flightapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airports(
    var airportCode: String?,
    var airportName: String?,
    var country: String?,
    var phoneNumber: String?,
    var latitudeLongitude: String?,
    var flightList: List<Flights>?
) : Parcelable
