package com.farez.storyapp.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences (private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey("TOKEN")
    private val auth = booleanPreferencesKey("AUTH")

    fun getToken() : Flow<String> {
        return dataStore.data.map {
            it[token] ?: "null"
        }
    }

    suspend fun saveToken(t: String) {
        dataStore.edit {
            it [token] = t
        }
    }

    suspend fun delToken() {
        dataStore.edit {
            it[token] = "null"
        }
    }

    suspend fun setAuth(b : Boolean) {
        dataStore.edit {
            it[auth] = b
        }
    }

    fun getAuth(): Flow<Boolean> {
        return dataStore.data.map {
            it[auth] ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}