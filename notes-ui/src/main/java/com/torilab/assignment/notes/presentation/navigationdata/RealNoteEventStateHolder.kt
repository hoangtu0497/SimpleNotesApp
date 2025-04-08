package com.torilab.assignment.notes.presentation.navigationdata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle

/**
 * The [SavedStateHandle] could not help me achieve what I needed, so I resolved to use this simple state holder implementation.
 */
internal class RealNoteEventStateHolder : NoteEventStateHolder {
    private var _event by mutableStateOf<NoteEventState?>(null)
    override val event: NoteEventState?
        get() {
            val current = _event
            _event = null
            return current
        }

    override fun notifyNoteAdded(noteId: Int) {
        _event = NoteAdded(noteId)
    }

    override fun notifyNoteUpdated(noteId: Int) {
        _event = NoteUpdated(noteId)
    }

    override fun notifyNoteDeleted(noteId: Int) {
        _event = NoteDeleted(noteId)
    }
}
