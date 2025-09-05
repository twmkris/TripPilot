
package com.vibecoding.trippilot.domain.usecase

import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.repository.TripRepository

class GetTripUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke(id: Int): Trip? {
        return repository.getTripById(id)
    }
}
