package beprogressive.airlinesclient.search.ui.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.beprogressive.it.airlinesclient.R
import beprogressive.airlinesclient.flights.network.model.FlightItem
import beprogressive.airlinesclient.search.ui.viewmodel.UiEvent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bind:number")
fun TextView.bindNumber(number: Int?): Int {
    val notNullValue = number ?: 0
    text = notNullValue.toString()
    return notNullValue
}

@BindingAdapter("bind:adultsCount", "bind:teensCount", "bind:childrenCount")
fun TextView.bindPassengers(adultsCount: Int?, teensCount: Int?, childrenCount: Int?) {
    text = if (adultsCount != null)
        resources.getString(
            R.string.passengers_desc,
            adultsCount,
            teensCount,
            childrenCount
        )
    else
        ""
}

@BindingAdapter("bind:originStation", "bind:destinationStation", "bind:date", "bind:adultsCount")
fun Button.bindSearchButton(
    originStation: String?,
    destinationStation: String?,
    date: Long?,
    adultsCount: Int?
) {
    isEnabled =
        originStation != null && destinationStation != null && date != null && adultsCount != null
}

@BindingAdapter("bind:readableDate")
fun TextView.bindReadableDate(
    date: Long?
) {
    text = if (date == null) "" else
        DateFormat.getDateInstance(DateFormat.LONG).format(Date(date))
}

@BindingAdapter("bind:flightReadableTime")
fun TextView.bindFlightReadableTime(
    date: String?
) {
    text = if (date == null) ""
    else
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            if (parsedDate == null) ""
            else
                DateFormat.getTimeInstance(DateFormat.SHORT).format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
}

@BindingAdapter("bind:routeTime")
fun TextView.bindRouteTime(
    time: String?
) {
    text = if (time == null) ""
    else
        try {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val parsedDate = dateFormat.parse(time)
            if (parsedDate == null) ""
            else
                DateFormat.getTimeInstance(DateFormat.SHORT).format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
}

@BindingAdapter("bind:showIfNoData")
fun TextView.showIfNoData(
    uiEvent: UiEvent
) {
    visibility = if (uiEvent is UiEvent.NoData) View.VISIBLE else View.GONE
}

@BindingAdapter("bind:fares")
fun TextView.bindFares(
    flightItem: FlightItem
) {
    text = resources.getString(
        R.string.amount,
        flightItem.fares.adult.amount,
        flightItem.fares.adult.currency
    )
}
