package beprogressive.airlinesclient.airports.network

import beprogressive.airlinesclient.airports.network.model.RouteResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutesService {

    @GET("/locate/v5/routes?fields=departureAirport.code,departureAirport.name,departureAirport.macCity.macCode,arrivalAirport.code,arrivalAirport.name,arrivalAirport.macCity.macCode,connectingAirport.code,connectingAirport.name,connectingAirport.macCity.macCode")
    suspend fun getRoutes(
        @Query(value = "departureAirportCode") departureAirportCode: String? = null,
        @Query(value = "arrivalAirportCode") arrivalAirportCode: String? = null
    ): List<RouteResponse>

    @GET("/locate/v5/routes?fields=departureAirport.code,departureAirport.name,departureAirport.macCity.macCode,arrivalAirport.code,arrivalAirport.name,arrivalAirport.macCity.macCode,connectingAirport.code,connectingAirport.name,connectingAirport.macCity.macCode")
    fun getRoutesRx(
        @Query(value = "departureAirportCode") departureAirportCode: String? = null,
        @Query(value = "arrivalAirportCode") arrivalAirportCode: String? = null
    ): Single<List<RouteResponse>>
}