package beprogressive.airlinesclient.search.ui.viewmodel

sealed class UiEvent {
    object Idle: UiEvent()
    object NoData: UiEvent()
}