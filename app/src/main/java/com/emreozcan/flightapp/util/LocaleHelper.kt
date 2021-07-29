package com.emreozcan.flightapp.util

import android.content.Context
import android.os.Build
import java.util.*
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.annotation.TargetApi
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList


class LocaleHelper{
    companion object{
        private val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        fun onAttach(context: Context): Context {
            val lang: String = getPersistedData(context, Locale.getDefault().language)
            return setLocale(context, lang)
        }

        fun onAttach(context: Context, defaultLanguage: String): Context {
            val lang: String = getPersistedData(context, defaultLanguage)
            return setLocale(context, lang)
        }

        fun getLanguage(context: Context): String {
            return getPersistedData(context, Locale.getDefault().language)
        }

        fun setLocale(context: Context, language: String): Context{
            persist(context,language)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return updateResources(context, language);
            }
            return updateResourcesLegacy(context, language);
        }

        private fun getPersistedData(context: Context, defaultLanguage: String): String {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)!!
        }

        private fun persist(context: Context, language: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }

        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
    }

}