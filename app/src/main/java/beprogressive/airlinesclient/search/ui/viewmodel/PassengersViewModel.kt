package beprogressive.airlinesclient.search.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import beprogressive.airlinesclient.search.ui.PassengersScreenArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PassengersViewModel @Inject constructor(state: SavedStateHandle) : ViewModel() {

    private val args = PassengersScreenArgs.fromSavedStateHandle(state)

    private val _adults: MutableStateFlow<Int> = MutableStateFlow(1)
    val adults: StateFlow<Int> = _adults
    private val _teens: MutableStateFlow<Int> = MutableStateFlow(0)
    val teens: StateFlow<Int> = _teens
    private val _children: MutableStateFlow<Int> = MutableStateFlow(0)
    val children: StateFlow<Int> = _children

    init {
        _adults.update {
            if (args.adultsCount != 0)
                args.adultsCount
            else
                it
        }

        _teens.update {
            args.teensCount
        }

        _children.update {
            args.childrenCount
        }
    }

    fun updateAdultsCount(increase: Boolean) {
        if (increase)
            increase(_adults)
        else
            decrease(_adults, 1)
    }

    fun updateTeensCount(increase: Boolean) {
        if (increase)
            increase(_teens)
        else
            decrease(_teens)
    }

    fun updateChildrenCount(increase: Boolean) {
        if (increase)
            increase(_children)
        else
            decrease(_children)
    }

    private fun increase(value: MutableStateFlow<Int>) {
        value.update {
            it.plus(1)
        }
    }

    private fun decrease(value: MutableStateFlow<Int>, minValue: Int = 0) {
        value.update {
            with(it) {
                if (this > minValue)
                    minus(1)
                else
                    this
            }
        }
    }

    fun getAdultsCount() = adults.value
    fun getTeensCount() = teens.value
    fun getChildrenCount() = children.value
}