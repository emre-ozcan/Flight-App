package com.emreozcan.flightapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flights(
    var companyName: String,
    var flightStartAndFinishTime: String,
    var capacity: String,
    var hour: String,
    var flightCode: String,
    var startAndTargetCode: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "")
}