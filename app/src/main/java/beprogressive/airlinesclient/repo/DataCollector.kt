package beprogressive.airlinesclient.repo

interface DataCollector {

    suspend fun collectAirports()

    suspend fun getDestinationCodes(departureAirportCode: String): List<String>

    suspend fun collectFlights(
        date: String,
        origin: String,
        destination: String,
        adult: Int,
        teen: Int,
        child: Int
    ): Boolean
}