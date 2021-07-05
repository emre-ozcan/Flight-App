package com.emreozcan.flightapp.models

data class User(
    var userName: String,
    var userSurname: String,
    var userEmail: String,
    var userPassword: String
){
    constructor() :this("","","","")
}