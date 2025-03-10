package com.example.lab4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _remindMeditation = MutableStateFlow(false)
    val remindMeditation: StateFlow<Boolean> get() = _remindMeditation

    private val _notifyNewMeditations = MutableStateFlow(false)
    val notifyNewMeditations: StateFlow<Boolean> get() = _notifyNewMeditations

    private val _notifyNewMusic = MutableStateFlow(false)
    val notifyNewMusic: StateFlow<Boolean> get() = _notifyNewMusic

    init {
        // Загружаем данные при инициализации ViewModel
        viewModelScope.launch {
            userPreferences.userName.collect { _name.value = it }
            userPreferences.userEmail.collect { _email.value = it }
            userPreferences.remindMeditation.collect { _remindMeditation.value = it }
            userPreferences.notifyNewMeditations.collect { _notifyNewMeditations.value = it }
            userPreferences.notifyNewMusic.collect { _notifyNewMusic.value = it }
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
        viewModelScope.launch {
            userPreferences.updateUserName(newName)
        }
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        viewModelScope.launch {
            userPreferences.updateUserEmail(newEmail)
        }
    }

    fun updateRemindMeditation(value: Boolean) {
        _remindMeditation.value = value
        viewModelScope.launch {
            userPreferences.updateRemindMeditation(value)
        }
    }

    fun updateNotifyNewMeditations(value: Boolean) {
        _notifyNewMeditations.value = value
        viewModelScope.launch {
            userPreferences.updateNotifyNewMeditations(value)
        }
    }

    fun updateNotifyNewMusic(value: Boolean) {
        _notifyNewMusic.value = value
        viewModelScope.launch {
            userPreferences.updateNotifyNewMusic(value)
        }
    }
}
class ProfileViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}