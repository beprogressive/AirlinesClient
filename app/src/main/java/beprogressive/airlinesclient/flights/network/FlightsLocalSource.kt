package beprogressive.airlinesclient.flights.network

import beprogressive.airlinesclient.flights.network.model.FlightItem
import kotlinx.coroutines.flow.Flow

interface FlightsLocalSource {

    fun insertAll(list: List<FlightItem>)

    fun deleteAll()

    fun observeAll(): Flow<List<FlightItem>>
}