
package com.vibecoding.trippilot.presentation.trip_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibecoding.trippilot.domain.usecase.GetTripsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TripListViewModel(
    getTripsUseCase: GetTripsUseCase
) : ViewModel() {

    val trips = getTripsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
