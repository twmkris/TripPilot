
package com.vibecoding.trippilot.domain.usecase

import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow

class GetTripsUseCase(
    private val repository: TripRepository
) {
    operator fun invoke(): Flow<List<Trip>> {
        return repository.getTrips()
    }
}
