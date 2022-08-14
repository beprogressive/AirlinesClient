package beprogressive.airlinesclient.repo

import beprogressive.airlinesclient.airports.local.AirportsLocalSource
import beprogressive.airlinesclient.core.ErrorHandler
import beprogressive.airlinesclient.flights.network.FlightsLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val dataCollector: DataCollector,
    private val airportsLocalSource: AirportsLocalSource,
    private val flightsLocalSource: FlightsLocalSource,
    private val errorHandler: ErrorHandler
) : SearchRepository {

    private companion object {
        const val REPEAT_DELAY = 3000L
    }

    override fun observeOrigins(countryCode: String) = airportsLocalSource.observeAll()

    override fun observeFlights() = flightsLocalSource.observeAll()

    override suspend fun getDestinationCodes(departureAirportCode: String): List<String>? {
        return try {
            dataCollector.getDestinationCodes(departureAirportCode)
        } catch (e: Exception) {
            if (onException(e)) {
                delay(REPEAT_DELAY)
                getDestinationCodes(departureAirportCode)
            } else
                null
        }
    }

    override fun observeDestinations(destinationCodes: List<String>) =
        airportsLocalSource.observeAirportList(destinationCodes)

    override suspend fun collectAirports() {
        withContext(Dispatchers.IO) {
            try {
                dataCollector.collectAirports()
            } catch (e: Exception) {
                if (onException(e)) {
                    delay(REPEAT_DELAY)
                    collectAirports()
                }
            }
        }
    }

    override suspend fun collectFlights(
        date: String,
        origin: String,
        destination: String,
        adult: Int,
        teen: Int,
        child: Int
    ): Boolean? = withContext(Dispatchers.IO) {
        flightsLocalSource.deleteAll()
        try {
            dataCollector.collectFlights(
                date,
                origin,
                destination,
                adult,
                teen,
                child
            )
        } catch (e: Exception) {
            if (onException(e)) {
                delay(REPEAT_DELAY)
                collectFlights(date, origin, destination, adult, teen, child)
            } else
                null
        }
    }

    override fun onException(throwable: Throwable) = errorHandler.onException(throwable)
}