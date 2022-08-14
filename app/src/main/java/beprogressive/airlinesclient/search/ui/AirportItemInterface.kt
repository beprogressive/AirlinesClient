package beprogressive.airlinesclient.search.ui

import beprogressive.airlinesclient.airports.local.model.AirportItem

interface AirportItemInterface {
    fun onItemClick(item: AirportItem)
}