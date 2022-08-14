package beprogressive.airlinesclient.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import beprogressive.airlinesclient.airports.local.model.AirportItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainViewModel : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String?>(0)
    val errorMessage: SharedFlow<String?> = _errorMessage

    private val _originStation = MutableStateFlow<AirportItem?>(null)
    val originStation: StateFlow<AirportItem?> = _originStation

    private val _destinationStation = MutableStateFlow<AirportItem?>(null)
    val destinationStation: StateFlow<AirportItem?> = _destinationStation

    private val _date = MutableStateFlow<Long?>(null)
    val date: StateFlow<Long?> = _date

    private val _adultsCount = MutableStateFlow<Int?>(null)
    val adultsCount: StateFlow<Int?> = _adultsCount

    private val _teensCount = MutableStateFlow(0)
    val teensCount: StateFlow<Int> = _teensCount

    private val _childrenCount = MutableStateFlow(0)
    val childrenCount: StateFlow<Int> = _childrenCount

    fun setErrorMessage(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
        }
    }

    fun getOriginCode() = originStation.value?.code?: ""

    fun setOrigin(originStation: AirportItem?) {
        _originStation.update {
            originStation
        }
        clearDestination()
    }

    private fun clearDestination() {
        setDestination(null)
    }

    fun setDestination(destinationStation: AirportItem?) {
        _destinationStation.update {
            destinationStation
        }
    }

    fun setDate(date: Long) {
        _date.update {
            date
        }
    }

    fun setAdultsCount(count: Int) {
        _adultsCount.update {
            count
        }
    }

    fun setTeensCount(count: Int) {
        _teensCount.update {
            count
        }
    }

    fun setChildren(count: Int) {
        _childrenCount.update {
            count
        }
    }

    fun isOriginSet() = originStation.value != null

    fun getRequestItem(): FlightRequestItem? {
        return FlightRequestItem.Creator.get(
            date = date.value?.toServerDateFormat(),
            origin = originStation.value?.code,
            destination = destinationStation.value?.code,
            adult = adultsCount.value,
            teen = teensCount.value,
            child = childrenCount.value
        )
    }

    private fun Long.toServerDateFormat(): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(this)
    }
}

class FlightRequestItem private constructor(
    val date: String,
    val origin: String,
    val destination: String,
    val adult: Int,
    val teen: Int,
    val child: Int,
) {
    object Creator {
        fun get(
            date: String?,
            origin: String?,
            destination: String?,
            adult: Int?,
            teen: Int?,
            child: Int?
        ): FlightRequestItem? {
            return getItem(date, origin, destination, adult, teen, child)
        }

        private fun getItem(
            date: String?,
            origin: String?,
            destination: String?,
            adult: Int?,
            teen: Int?,
            child: Int?
        ): FlightRequestItem? {
            return if (date != null && origin != null && destination != null && adult != null && teen != null && child != null)
                return FlightRequestItem(
                    date = date,
                    origin = origin,
                    destination = destination,
                    adult = adult,
                    teen = teen,
                    child = child
                )
            else null
        }
    }
}