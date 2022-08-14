package beprogressive.airlinesclient.search.ui

import beprogressive.airlinesclient.flights.network.model.FlightItem

interface FlightItemInterface {
    fun onItemClick(item: FlightItem)
}