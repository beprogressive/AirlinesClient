package beprogressive.airlinesclient.di

import android.content.Context
import androidx.room.Room
import beprogressive.airlinesclient.airports.local.AirportsLocalSource
import beprogressive.airlinesclient.core.AppDatabase
import beprogressive.airlinesclient.core.AppDatabase.Companion.DATABASE_NAME
import beprogressive.airlinesclient.flights.network.FlightsLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Singleton
    @Provides
    fun provideAirportsDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideAirportLocalSource(dataBase: AppDatabase): AirportsLocalSource =
        dataBase.airportItemDao()

    @Singleton
    @Provides
    fun provideFlightLocalSource(dataBase: AppDatabase): FlightsLocalSource =
        dataBase.flightItemDao()
}