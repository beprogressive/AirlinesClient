package beprogressive.airlinesclient.airports.local

import beprogressive.airlinesclient.airports.local.model.AirportItem
import kotlinx.coroutines.flow.Flow

interface AirportsLocalSource {
    fun observeAll(): Flow<List<AirportItem>>
    fun deleteAll()
    fun insertAll(list: List<AirportItem>)
    suspend fun getAirport(airportCode: String): AirportItem?
    suspend fun getAirportList(codeList: List<String>): List<AirportItem>
    fun observeAirportList(codeList: List<String>): Flow<List<AirportItem>>
    suspend fun deleteExpires(validCodeList: List<String>)
}