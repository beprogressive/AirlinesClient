package beprogressive.airlinesclient.search.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import beprogressive.airlinesclient.repo.SearchRepository
import beprogressive.airlinesclient.search.ui.SearchAirportScreenArgs
import beprogressive.airlinesclient.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val args = SearchAirportScreenArgs.fromSavedStateHandle(state)

    suspend fun getDestinationCodes() = repository.getDestinationCodes(args.code)

    fun getDestinations(destinationCodes: List<String>) =
        repository.observeDestinations(destinationCodes)
            .handleErrors {
                repository.onException(it)
            }

    fun getOrigins() = repository.observeOrigins(args.code).handleErrors {
        repository.onException(it)
    }

    init {
        if (!args.showDestinations)
            collectOrigins()
    }

    private fun collectOrigins() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.collectAirports()
        }
    }

    fun isDestinationScope() = args.showDestinations
}

