
package com.vibecoding.trippilot.domain.usecase

import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.repository.TripRepository

class DeleteTripUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) {
        repository.deleteTrip(trip)
    }
}
