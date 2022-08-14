package beprogressive.airlinesclient.repo

import beprogressive.airlinesclient.airports.local.model.AirportItem
import beprogressive.airlinesclient.flights.network.model.FlightItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun observeOrigins(countryCode: String): Flow<List<AirportItem>>

    fun observeDestinations(destinationCodes: List<String>): Flow<List<AirportItem>>

    fun observeFlights(): Flow<List<FlightItem>>

    suspend fun collectAirports()

    /** Returns `false` if data is empty */
    suspend fun collectFlights(
        date: String,
        origin: String,
        destination: String,
        adult: Int,
        teen: Int,
        child: Int
    ): Boolean?

    suspend fun getDestinationCodes(departureAirportCode: String): List<String>?

    /** Returns `true` if network trouble */
    fun onException(throwable: Throwable): Boolean
}