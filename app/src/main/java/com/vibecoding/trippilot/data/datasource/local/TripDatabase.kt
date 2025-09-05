
package com.vibecoding.trippilot.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TripEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TripDatabase : RoomDatabase() {

    abstract val tripDao: TripDao

    companion object {
        const val DATABASE_NAME = "trips_db"
    }
}
