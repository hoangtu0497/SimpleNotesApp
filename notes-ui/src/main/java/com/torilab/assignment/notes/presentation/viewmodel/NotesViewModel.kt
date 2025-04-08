package com.torilab.assignment.notes.presentation.viewmodel

import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.viewmodel.StateViewModel

internal interface NotesViewModel : StateViewModel<NotesScreenState> {
    fun initialLoadNotes()
    fun loadMoreNotes()

    fun onNoteAdded(id: Int)
    fun onNoteUpdated(id: Int)
    fun onNoteDeleted(id: Int)
}

internal data class NotesScreenState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
)
