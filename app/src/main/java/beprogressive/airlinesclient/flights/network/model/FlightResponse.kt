package beprogressive.airlinesclient.flights.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlightResponse(
    @Json(name = "currency") val currency: String?,
    @Json(name = "currPrecision") val currPrecision: Int?,
    @Json(name = "trips") val trips: List<Trip>?
)

data class Trip(
    @Json(name = "dates") val dates: List<TripDate>?,
    @Json(name = "origin") val origin: String?,
    @Json(name = "originName") val originName: String?,
    @Json(name = "destination") val destination: String?,
    @Json(name = "destinationName") val destinationName: String?
)

data class TripDate(
    @Json(name = "dateOut") val dateOut: String?,
    @Json(name = "flights") val flights: List<TripFlight>?
)

data class TripFlight(
    @Json(name = "faresLeft") val faresLeft: Int?,
    @Json(name = "regularFare") val regularFare: RegularFare?,
    @Json(name = "flightNumber") val flightNumber: String?,
    @Json(name = "time") val dateTimes: List<String>?,
    @Json(name = "duration") val duration: String?,
    @Json(name = "segments") val segments: List<Segment>?,
    @Json(name = "operatedBy") val operatedBy: String?
)

data class RegularFare(
    @Json(name = "fares") val fares: List<TripFare>?
)

data class TripFare(
    @Json(name = "type") val type: String?,
    @Json(name = "amount") val amount: Double?,
    @Json(name = "count") val count: Int?
)

data class Segment(
    @Json(name = "origin") val origin: String?,
    @Json(name = "destination") val destination: String?,
    @Json(name = "flightNumber") val flightNumber: String?,
    @Json(name = "time") val dateTimes: List<String>?,
    @Json(name = "duration") val duration: String?
)