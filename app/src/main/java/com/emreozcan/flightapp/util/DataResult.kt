package com.emreozcan.flightapp.util

sealed class DataResult(val errorMessage: String? =null) {
    class Success(): DataResult()
    class Error(errorMessage: String?): DataResult(errorMessage)
    class Loading(): DataResult()
}