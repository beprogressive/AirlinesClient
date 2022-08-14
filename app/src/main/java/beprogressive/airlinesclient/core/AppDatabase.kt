package beprogressive.airlinesclient.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import beprogressive.airlinesclient.airports.local.model.AirportItem
import beprogressive.airlinesclient.airports.local.model.AirportItemDao
import beprogressive.airlinesclient.flights.network.model.FlightItem
import beprogressive.airlinesclient.flights.network.model.FlightItemDao
import beprogressive.airlinesclient.flights.network.model.MapConverter

@Database(entities = [AirportItem::class, FlightItem::class], version = 1)
@TypeConverters(MapConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "airports_database.db"
    }
    abstract fun airportItemDao(): AirportItemDao
    abstract fun flightItemDao(): FlightItemDao
}