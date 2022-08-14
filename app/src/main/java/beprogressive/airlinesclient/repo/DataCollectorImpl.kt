package beprogressive.airlinesclient.repo

import beprogressive.airlinesclient.airports.local.AirportsLocalSource
import beprogressive.airlinesclient.airports.local.model.AirportItem
import beprogressive.airlinesclient.airports.network.AirportService
import beprogressive.airlinesclient.airports.network.RoutesService
import beprogressive.airlinesclient.airports.network.model.AirportResponse
import beprogressive.airlinesclient.flights.network.FlightService
import beprogressive.airlinesclient.flights.network.FlightsLocalSource
import beprogressive.airlinesclient.flights.network.model.Fares
import beprogressive.airlinesclient.flights.network.model.FlightItem
import beprogressive.airlinesclient.flights.network.model.FlightResponse
import beprogressive.airlinesclient.flights.network.model.TripFare

class DataCollectorImpl(
    private val airportService: AirportService,
    private val routesService: RoutesService,
    private val flightService: FlightService,
    private val airportsLocalSource: AirportsLocalSource,
    private val flightsLocalSource: FlightsLocalSource
) : DataCollector {

    override suspend fun collectAirports() = updateItemsInDb(airportService.getAirports(""))

    override suspend fun getDestinationCodes(departureAirportCode: String): List<String> {
        return routesService.getRoutes(departureAirportCode)
            .mapNotNull {
                it.arrivalAirport?.code
            }
    }

    override suspend fun collectFlights(
        date: String,
        origin: String,
        destination: String,
        adult: Int,
        teen: Int,
        child: Int
    ): Boolean {
        val items = flightResponseToFlightItems(
            flightService.getFlights(
                date,
                origin,
                destination,
                adult,
                teen,
                child
            )
        )
        flightsLocalSource.insertAll(
            items
        )
        return items.isNotEmpty()
    }

    private fun flightResponseToFlightItems(flightResponse: FlightResponse): List<FlightItem> {
        return flightResponse.trips?.flatMap { trip ->
            trip.dates?.flatMap { tripDate ->
                tripDate.flights?.map { tripFlight ->
                    val fares =
                        getFares(tripFlight.regularFare!!.fares, flightResponse.currency ?: "")
                    FlightItem(
                        name = tripFlight.flightNumber ?: "",
                        originDate = tripFlight.dateTimes?.get(0),
                        destinationDate = tripFlight.dateTimes?.get(1),
                        originCode = trip.origin,
                        originName = trip.originName,
                        destinationCode = trip.destination,
                        destinationName = trip.destinationName,
                        routeTime = tripFlight.duration,
                        fares = fares
                    )
                }.orEmpty()
            }.orEmpty()
        } ?: ArrayList()
    }

    private fun getFares(tripFares: List<TripFare>?, currency: String): Fares {
        val fares = Fares()
        tripFares?.forEach { tripFare ->
            when (tripFare.type) {
                "ADT" -> {
                    fares.adult = Fares.Adult(
                        tripFare.count ?: 0,
                        tripFare.amount?.toFloat() ?: 0F,
                        currency
                    )
                }
                "TEEN" -> {
                    fares.teen = Fares.Teen(
                        tripFare.count ?: 0,
                        tripFare.amount?.toFloat() ?: 0F,
                        currency
                    )
                }
                "CHD" -> {
                    fares.child = Fares.Child(
                        tripFare.count ?: 0,
                        tripFare.amount?.toFloat() ?: 0F,
                        currency
                    )
                }
            }
        }
        return fares
    }

    private suspend fun updateItemsInDb(list: List<AirportResponse>) {
        airportsLocalSource.deleteExpires(list.mapNotNull {
            it.code
        })

        airportsLocalSource.insertAll(
            list.mapNotNull {
                it.toAirportItem()
            }
        )
    }

    private fun AirportResponse.toAirportItem(): AirportItem? {
        code?.let {
            return AirportItem(
                code = code,
                name = name,
                countryName = country?.name,
                countryCode = country?.code,
            )
        }
        return null
    }
}