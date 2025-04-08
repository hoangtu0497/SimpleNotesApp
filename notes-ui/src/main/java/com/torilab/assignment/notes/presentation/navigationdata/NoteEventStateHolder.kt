package com.torilab.assignment.notes.presentation.navigationdata

interface NoteEventStateHolder {
    val event: NoteEventState?

    fun notifyNoteAdded(noteId: Int)
    fun notifyNoteUpdated(noteId: Int)
    fun notifyNoteDeleted(noteId: Int)
}
