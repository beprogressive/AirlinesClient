package beprogressive.airlinesclient.search.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import beprogressive.airlinesclient.repo.SearchRepository
import beprogressive.airlinesclient.search.ui.ResultScreenArgs
import beprogressive.airlinesclient.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: SearchRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val args = ResultScreenArgs.fromSavedStateHandle(state)

    private val _uiEvent: MutableStateFlow<UiEvent> = MutableStateFlow(UiEvent.Idle)
    val uiEvent: StateFlow<UiEvent> = _uiEvent.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            repository.collectFlights(
                date = args.date,
                origin = args.origin,
                destination = args.destination,
                adult = args.adult,
                teen = args.teen,
                child = args.child
            )?.let {
                if (it) uiEventIdle() else uiEventNoData()
            }
        }
    }

    fun getFlights() = repository.observeFlights().handleErrors {
        repository.onException(it)
    }

    private fun uiEventIdle() {
        _uiEvent.update {
            UiEvent.Idle
        }
    }

    private fun uiEventNoData() {
        _uiEvent.update {
            UiEvent.NoData
        }
    }
}