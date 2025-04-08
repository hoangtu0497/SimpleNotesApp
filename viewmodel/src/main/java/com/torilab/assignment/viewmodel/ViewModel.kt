package com.torilab.assignment.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface StateViewModel<State> {
    val state: StateFlow<State>
}

interface EventViewModel<ViewEvent> {
    val viewEvent: Flow<ViewEvent>
}

class StateDelegate<State>(defaultState: State) : StateViewModel<State> {
    private val _state = MutableStateFlow(defaultState)
    override val state: StateFlow<State> = _state.asStateFlow()

    fun updateState(block: (State) -> State) {
        _state.update {
            block(it)
        }
    }
}

class EventDelegate<ViewEvent> : EventViewModel<ViewEvent> {
    private val _viewEvent = MutableSharedFlow<ViewEvent>()
    override val viewEvent: Flow<ViewEvent> = _viewEvent.asSharedFlow()

    suspend fun sendEvent(newEvent: ViewEvent) {
        _viewEvent.emit(newEvent)
    }
}
