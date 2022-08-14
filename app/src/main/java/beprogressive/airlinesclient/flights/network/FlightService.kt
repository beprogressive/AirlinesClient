package beprogressive.airlinesclient.flights.network

import beprogressive.airlinesclient.flights.network.model.FlightResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightService {

    // example query: booking/v4/en-gb/Availability?dateout=2022-05-16&origin=WRO&destination=DUB&adt=1&ToUs=AGREED
    @GET("booking/v4/en-gb/Availability")
    suspend fun getFlights(
        @Query("dateout") date: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("adt") adult: Int,
        @Query("teen") teen: Int,
        @Query("chd") child: Int,
        @Query("inf") inf: Int = 0,
        @Query("ToUs") toUs: String = "AGREED"
    ): FlightResponse

    @GET("booking/v4/en-gb/Availability")
    fun getFlightsRx(
        @Query("dateout") date: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("adt") adult: Int,
        @Query("teen") teen: Int,
        @Query("chd") child: Int,
        @Query("inf") inf: Int = 0,
        @Query("ToUs") toUs: String = "AGREED"
    ): Single<FlightResponse>
}