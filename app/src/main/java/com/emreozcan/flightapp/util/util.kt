package com.emreozcan.flightapp.util

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

fun showFieldSnackbar(view: View){
    Snackbar.make(view,"Field/s could not be empty", Snackbar.LENGTH_LONG).setAction("Okay"){}.show()
}
