package com.emreozcan.flightapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emreozcan.flightapp.util.Constants.Companion.DATASTORE_PREFERENCE_NAME
import com.emreozcan.flightapp.util.Constants.Companion.LANGUAGE_PREFERENCE_KEY
import com.emreozcan.flightapp.util.Constants.Companion.ONBOARDING_PREFERENCE_KEY
import com.google.firebase.firestore.remote.Datastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_PREFERENCE_NAME)

class DataStoreRepository(val context: Context){

    private val ONBOARDING = booleanPreferencesKey(ONBOARDING_PREFERENCE_KEY)
    private val LANGUAGE = stringPreferencesKey(LANGUAGE_PREFERENCE_KEY)

    val readOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING] ?: false
    }

    val readLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "sys"
    }

    suspend fun onBoardingShowed(){
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING] = true
        }
    }

    suspend fun setLanguage(languageCode: String){
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = languageCode
        }
    }

}