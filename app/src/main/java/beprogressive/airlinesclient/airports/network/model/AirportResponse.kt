package beprogressive.airlinesclient.airports.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AirportResponse(
    @Json(name = "code") val code: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "seoName") val seoName: String?,
    @Json(name = "base") val isBase: Boolean?,
    @Json(name = "timeZone") val timeZone: String?,
    @Json(name = "city") val city: City?,
    @Json(name = "macCity") val macCity: MacCity?,
    @Json(name = "region") val region: Region?,
    @Json(name = "country") val country: Country?,
    @Json(name = "coordinates") val coordinates: Coordinates?
) {
    data class City(
        @Json(name = "code") val code: String?,
        @Json(name = "name") val name: String?
    )

    data class MacCity(
        @Json(name = "code") val code: String?,
        @Json(name = "macCode") val macCode: String?,
        @Json(name = "name") val name: String?
    )

    data class Region(
        @Json(name = "code") val code: String?,
        @Json(name = "name") val name: String?
    )

    data class Country(
        @Json(name = "code") val code: String?,
        @Json(name = "name") val name: String?,
        @Json(name = "currency") val currencyCode: String?
    )

    data class Coordinates(
        @Json(name = "latitude") val latitude: Double?,
        @Json(name = "longitude") val longitude: Double?
    )
}
