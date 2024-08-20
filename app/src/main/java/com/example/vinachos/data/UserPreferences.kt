package com.example.vinachos.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Extensión de Context para obtener DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    // Define your preferences keys
    private val USERS_KEY = stringPreferencesKey("users")

    private fun updateUserList(userPreferences: UserPreferences, newUserList: List<UserData>) {
        CoroutineScope(Dispatchers.IO).launch {
            userPreferences.saveUserPreferences(newUserList)
        }
    }

    // Functions to save and read preferences
    suspend fun saveUserPreferences(users: List<UserData>) {
        val jsonString = Gson().toJson(users)
        dataStore.edit { preferences ->
            preferences[USERS_KEY] = jsonString
        }
    }

    val userPreferencesFlow: Flow<List<UserData>> = dataStore.data
        .map { preferences ->
            val jsonString = preferences[USERS_KEY] ?: "[]"
            val userListType = object : TypeToken<List<UserData>>() {}.type
            Gson().fromJson(jsonString, userListType)
        }
}