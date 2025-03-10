package com.example.lab4

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val REMIND_MEDITATION = booleanPreferencesKey("remind_meditation")
        val NOTIFY_NEW_MEDITATIONS = booleanPreferencesKey("notify_new_meditations")
        val NOTIFY_NEW_MUSIC = booleanPreferencesKey("notify_new_music")
    }

    // Получение имени пользователя
    val userName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    // Получение email пользователя
    val userEmail: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL] ?: ""
        }

    // Получение настроек уведомлений
    val remindMeditation: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[REMIND_MEDITATION] ?: false
        }

    val notifyNewMeditations: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[NOTIFY_NEW_MEDITATIONS] ?: false
        }

    val notifyNewMusic: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[NOTIFY_NEW_MUSIC] ?: false
        }

    // Сохранение имени пользователя
    suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    // Сохранение email пользователя
    suspend fun updateUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    // Сохранение настроек уведомлений
    suspend fun updateRemindMeditation(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMIND_MEDITATION] = value
        }
    }

    suspend fun updateNotifyNewMeditations(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFY_NEW_MEDITATIONS] = value
        }
    }

    suspend fun updateNotifyNewMusic(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFY_NEW_MUSIC] = value
        }
    }
}