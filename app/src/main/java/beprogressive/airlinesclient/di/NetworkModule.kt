package beprogressive.airlinesclient.di

import com.gmail.beprogressive.it.airlinesclient.BuildConfig
import beprogressive.airlinesclient.airports.network.AirportService
import beprogressive.airlinesclient.airports.network.RoutesService
import beprogressive.airlinesclient.flights.network.FlightService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AirportsRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class FlightsRetrofit

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(loggingInterceptor)
            }
        }.build()

    @Singleton
    @AirportsRetrofit
    @Provides
    fun provideAirportsRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://services-api.ryanair.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .build()

    @Singleton
    @FlightsRetrofit
    @Provides
    fun provideFlightsRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.ryanair.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideAirportService(@AirportsRetrofit retrofit: Retrofit): AirportService =
        retrofit.create(AirportService::class.java)

    @Singleton
    @Provides
    fun provideRoutesService(@AirportsRetrofit retrofit: Retrofit): RoutesService =
        retrofit.create(RoutesService::class.java)

    @Singleton
    @Provides
    fun provideFlightService(@FlightsRetrofit retrofit: Retrofit): FlightService =
        retrofit.create(FlightService::class.java)
}