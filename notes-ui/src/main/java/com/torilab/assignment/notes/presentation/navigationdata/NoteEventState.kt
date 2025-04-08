package com.torilab.assignment.notes.presentation.navigationdata

sealed interface NoteEventState {
    val noteId: Int
}

data class NoteAdded(override val noteId: Int) : NoteEventState
data class NoteUpdated(override val noteId: Int) : NoteEventState
data class NoteDeleted(override val noteId: Int) : NoteEventState
