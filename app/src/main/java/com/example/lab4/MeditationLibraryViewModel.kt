package com.example.lab4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*data class Meditation(
    val id: Int,
    val name: String,
    val imageUrl: String? = null
)*/

class MeditationLibraryViewModel(
    private val repository: MeditationRepository // Добавляем репозиторий
) : ViewModel() {

    private val _meditations = MutableStateFlow<List<APIMeditation>>(emptyList())
    val meditations: StateFlow<List<APIMeditation>> get() = _meditations

    private val _selectedMeditation = MutableStateFlow<APIMeditation?>(null)
    val selectedMeditation: StateFlow<APIMeditation?> get() = _selectedMeditation

    private val _meditationDetails = MutableStateFlow<APIMeditationDetail?>(null)
    val meditationDetails: StateFlow<APIMeditationDetail?> get() = _meditationDetails

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        loadMeditations() // Загружаем медитации при инициализации
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onMeditationSelected(meditation: APIMeditation) {
        _selectedMeditation.value = meditation
        loadMeditationDetails(meditation.id)
    }

    fun getFilteredMeditations(): List<APIMeditation> {
        return if (_searchQuery.value.isEmpty()) {
            _meditations.value
        } else {
            _meditations.value.filter { it.title.contains(_searchQuery.value, ignoreCase = true) }
        }
    }

    fun loadMeditations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _meditations.value = repository.getMeditations()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectMeditation(meditation: APIMeditation) {
        _selectedMeditation.value = meditation
        loadMeditationDetails(meditation.id)
    }

    private fun loadMeditationDetails(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _meditationDetails.value = repository.getMeditationDetails(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки деталей: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedMeditation() {
        _selectedMeditation.value = null
        _meditationDetails.value = null
    }
}