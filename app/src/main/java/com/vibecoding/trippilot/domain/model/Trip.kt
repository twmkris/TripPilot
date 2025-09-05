
package com.vibecoding.trippilot.domain.model

data class Trip(
    val id: Int = 0,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val budget: Double,
    val interests: List<String>,
    val itinerary: String
)
