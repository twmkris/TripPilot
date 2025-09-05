
package com.vibecoding.trippilot.data.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.vibecoding.trippilot.data.datasource.local.TripDao
import com.vibecoding.trippilot.data.datasource.local.TripEntity
import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(
    private val dao: TripDao,
    private val generativeModel: GenerativeModel
) : TripRepository {

    override fun getTrips(): Flow<List<Trip>> {
        return dao.getTrips().map { entities ->
            entities.map { it.toTrip() }
        }
    }

    override suspend fun getTripById(id: Int): Trip? {
        return dao.getTripById(id)?.toTrip()
    }

    override suspend fun insertTrip(trip: Trip) {
        dao.insertTrip(trip.toTripEntity())
    }

    override suspend fun deleteTrip(trip: Trip) {
        dao.deleteTrip(trip.toTripEntity())
    }

    override suspend fun generateItinerary(trip: Trip): String {
        val prompt = "Create a travel itinerary for a trip to ${trip.destination} from ${trip.startDate} to ${trip.endDate}. The budget is ${trip.budget} and the interests are ${trip.interests.joinToString(", ")}. **Please provide the response in Traditional Chinese.**"
        /**
        val prompt = """你是一位旅遊行程規劃專家。
        請根據以下資訊，為我規劃一趟詳細的旅遊行程。
        請以正體中文回答，並將結果以表格形式呈現，表格應包含「日期」、「行程內容」、「預估花費」和「備註」四個欄位。
        若行程天數較長，請分日規劃，並在「備註」欄位提供交通方式建議、當地特色或注意事項。

        ---
        旅遊資訊
        目的地：${trip.destination}
        旅遊日期：${trip.startDate} 至 ${trip.endDate}
        預算：${trip.budget}
        興趣：${trip.interests.joinToString(", ")}
        """
        **/
        Log.d(TAG, "prompt = $prompt")
        val response = generativeModel.generateContent(prompt)
        Log.d(TAG, "response = ${response.text}")
        return response.text ?: ""
    }

    companion object {
        private const val TAG = "TripRepository"
    }
}

private fun TripEntity.toTrip(): Trip {
    return Trip(
        id = id,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        budget = budget,
        interests = interests,
        itinerary = itinerary
    )
}

private fun Trip.toTripEntity(): TripEntity {
    return TripEntity(
        id = id,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        budget = budget,
        interests = interests,
        itinerary = itinerary
    )
}
