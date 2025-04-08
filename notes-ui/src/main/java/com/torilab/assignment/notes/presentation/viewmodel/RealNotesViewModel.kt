package com.torilab.assignment.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.torilab.assignment.foundations.onSuccess
import com.torilab.assignment.foundations.util.launchWithExceptionHandler
import com.torilab.assignment.note.domain.usecase.GetNoteDetail
import com.torilab.assignment.note.domain.usecase.GetNotes
import com.torilab.assignment.viewmodel.StateDelegate
import com.torilab.assignment.viewmodel.StateViewModel

internal class RealNotesViewModel(
    private val getNotes: GetNotes,
    private val getNoteDetail: GetNoteDetail,
    private val stateDelegate: StateDelegate<NotesScreenState>,
) : StateViewModel<NotesScreenState> by stateDelegate,
    NotesViewModel,
    ViewModel() {

    private var didFetchInitialNotes = false
    private var didLoadAllNotes = false

    private val currentNotesCount
        get() = state.value.notes.size

    override fun initialLoadNotes() {
        if (didFetchInitialNotes) return

        viewModelScope.launchWithExceptionHandler(onException = {
            stateDelegate.updateState { it.copy(isLoading = false) }
        }) {
            stateDelegate.updateState { it.copy(isLoading = true, notes = emptyList()) }
            getNotes(PAGE_SIZE, 0).onSuccess { fetchedNotes ->
                stateDelegate.updateState { it.copy(notes = fetchedNotes) }
                didFetchInitialNotes = true
            }

            stateDelegate.updateState { it.copy(isLoading = false) }
        }
    }

    override fun loadMoreNotes() {
        if (didLoadAllNotes) return

        viewModelScope.launchWithExceptionHandler(onException = {
            stateDelegate.updateState { it.copy(isLoading = false) }
        }) {
            stateDelegate.updateState { it.copy(isLoading = true) }
            getNotes(PAGE_SIZE, currentNotesCount).onSuccess { fetchedNotes ->
                if (fetchedNotes.isEmpty()) {
                    didLoadAllNotes = true
                    return@onSuccess
                }
                stateDelegate.updateState { it.copy(notes = it.notes + fetchedNotes) }
            }

            stateDelegate.updateState { it.copy(isLoading = false) }
        }
    }

    override fun onNoteAdded(id: Int) {
        viewModelScope.launchWithExceptionHandler(onException = {
            stateDelegate.updateState { it.copy(isLoading = false) }
        }) {
            stateDelegate.updateState { it.copy(isLoading = true) }
            getNoteDetail(id).onSuccess { newNote ->
                stateDelegate.updateState { it.copy(notes = listOf(newNote) + it.notes) }
            }

            stateDelegate.updateState { it.copy(isLoading = false) }
        }
    }

    override fun onNoteUpdated(id: Int) {
        viewModelScope.launchWithExceptionHandler(onException = {
            stateDelegate.updateState { it.copy(isLoading = false) }
        }) {
            stateDelegate.updateState { it.copy(isLoading = true) }
            getNoteDetail(id).onSuccess { updatedNote ->
                stateDelegate.updateState { state ->
                    state.copy(notes = state.notes.map { if (it.id == updatedNote.id) updatedNote else it })
                }
            }

            stateDelegate.updateState { it.copy(isLoading = false) }
        }
    }

    override fun onNoteDeleted(id: Int) {
        stateDelegate.updateState { state -> state.copy(notes = state.notes.filterNot { it.id == id }) }
    }
}

private const val PAGE_SIZE = 10
