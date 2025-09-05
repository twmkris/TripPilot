package com.vibecoding.trippilot.presentation.trip_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.usecase.GetTripUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripDetailViewModel(
    private val getTripUseCase: GetTripUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _trip = MutableStateFlow<Trip?>(null)
    val trip: StateFlow<Trip?> = _trip.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val tripId = savedStateHandle.get<Int>("tripId")
        if (tripId != null) {
            loadTrip(tripId)
        } else {
            _error.value = "Invalid trip ID"
        }
    }

    private fun loadTrip(tripId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val trip = getTripUseCase(tripId)
                _trip.value = trip
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load trip"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 