package com.perlovka.mastermindgame.model

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.MutablePreferences
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
//Create the dataStore
private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreference(private val context: Context) {
    private val dataStore = context.dataStore

    //Get user total score
    val userTotalScore: Flow<Int> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
            preferences[USER_TOTAL_SCORE] ?: 0
        }
    //Save score value
    suspend fun incrementTotalScore(score: Int) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[USER_TOTAL_SCORE] = score
        }
    }

    //Create key
    companion object {
        private val USER_TOTAL_SCORE = intPreferencesKey("user_total_score")
    }


}