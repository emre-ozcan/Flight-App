package com.emreozcan.flightapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userName: String,
    var userSurname: String,
    var userEmail: String,
    var userPassword: String,
    val flightHistoryList: List<Flights>
):Parcelable