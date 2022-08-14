package beprogressive.airlinesclient.core

interface ErrorHandler {
    fun onException(throwable: Throwable): Boolean
}