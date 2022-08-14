package beprogressive.airlinesclient.di

import beprogressive.airlinesclient.airports.local.AirportsLocalSource
import beprogressive.airlinesclient.airports.network.AirportService
import beprogressive.airlinesclient.airports.network.RoutesService
import beprogressive.airlinesclient.core.ErrorHandlerImpl
import beprogressive.airlinesclient.flights.network.FlightService
import beprogressive.airlinesclient.flights.network.FlightsLocalSource
import beprogressive.airlinesclient.repo.DataCollector
import beprogressive.airlinesclient.repo.DataCollectorImpl
import beprogressive.airlinesclient.repo.SearchRepository
import beprogressive.airlinesclient.repo.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun provideDataCollector(
        airportService: AirportService,
        routesService: RoutesService,
        flightService: FlightService,
        airportsLocalSource: AirportsLocalSource,
        flightsLocalSource: FlightsLocalSource,
    ): DataCollector {
        return DataCollectorImpl(
            airportService,
            routesService,
            flightService,
            airportsLocalSource,
            flightsLocalSource
        )
    }

    @Provides
    fun provideStationSearchRepository(
        dataCollector: DataCollector,
        airportsLocalSource: AirportsLocalSource,
        flightsLocalSource: FlightsLocalSource,
        errorHandler: ErrorHandlerImpl
    ): SearchRepository {
        return SearchRepositoryImpl(
            dataCollector,
            airportsLocalSource,
            flightsLocalSource,
            errorHandler
        )
    }
}