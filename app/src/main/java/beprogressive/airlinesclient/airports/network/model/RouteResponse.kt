package beprogressive.airlinesclient.airports.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteResponse(
    @Json(name = "departureAirport") val departureAirport: Airport.Departure?,
    @Json(name = "arrivalAirport") val arrivalAirport: Airport.Arrival?,
    @Json(name = "connectingAirport") val connectingAirport: Airport.Connecting?
) {

    sealed interface Airport {

        @Json(name = "code")
        val code: String?

        @Json(name = "name")
        val name: String?

        @Json(name = "macCity")
        val macCity: MacCity?

        data class Departure(
            override val code: String?,
            override val name: String?,
            override val macCity: MacCity?,
        ) : Airport

        data class Arrival(
            override val code: String?,
            override val name: String?,
            override val macCity: MacCity?
        ) : Airport

        data class Connecting(
            override val code: String?,
            override val name: String?,
            override val macCity: MacCity?
        ) : Airport

        data class MacCity(
            @Json(name = "macCode") val macCode: String?
        )
    }
}
