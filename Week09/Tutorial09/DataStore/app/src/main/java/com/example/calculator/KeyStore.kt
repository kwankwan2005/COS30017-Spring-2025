package com.example.calculator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KeyStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("savedCalcs")
        private val NUMBER1_KEY = intPreferencesKey("last_number1")
        private val NUMBER2_KEY = intPreferencesKey("last_number2")
    }

    val getLastNumber1: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[NUMBER1_KEY] ?: 0
    }

    val getLastNumber2: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[NUMBER2_KEY] ?: 0
    }

    suspend fun saveNumbers(number1: Int?, number2: Int?) {
        context.dataStore.edit { preferences ->
            number1?.let {
                preferences[NUMBER1_KEY] = it
            }
            number2?.let {
                preferences[NUMBER2_KEY] = it
            }
        }
    }

}