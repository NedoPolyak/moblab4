package com.example.lab4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Meditation(
    val id: Int,
    val name: String,
    val imageUrl: String? = null
)

class MeditationLibraryViewModel : ViewModel() {

    private val _meditations = MutableStateFlow<List<Meditation>>(emptyList())
    val meditations: StateFlow<List<Meditation>> get() = _meditations

    private val _selectedMeditation = MutableStateFlow<Meditation?>(null)
    val selectedMeditation: StateFlow<Meditation?> get() = _selectedMeditation

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    init {
        _meditations.value = listOf(
            Meditation(1, "Legendary Mechanic"),
            Meditation(2, "Lord of The Mysteries"),
            Meditation(3, "Soul of Negary"),
            Meditation(4, "The First Vampire"),
            Meditation(5, "Circle of Inevitability"),
            Meditation(6, "Mother of Learning")
        )
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onMeditationSelected(meditation: Meditation) {
        _selectedMeditation.value = meditation
    }

    fun getFilteredMeditations(): List<Meditation> {
        return if (_searchQuery.value.isEmpty()) {
            _meditations.value
        } else {
            _meditations.value.filter { it.name.contains(_searchQuery.value, ignoreCase = true) }
        }
    }
}