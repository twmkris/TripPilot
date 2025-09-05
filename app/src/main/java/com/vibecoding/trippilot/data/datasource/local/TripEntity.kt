
package com.vibecoding.trippilot.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val budget: Double,
    val interests: List<String>,
    val itinerary: String
)
