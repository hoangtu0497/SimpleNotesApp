package com.torilab.assignment.mynoteapp.app.di

import android.content.Context
import com.torilab.assignment.addnote.di.AddNoteUIDI
import com.torilab.assignment.note.di.NoteComponentDI
import com.torilab.assignment.notedetail.di.NoteDetailUIDI
import com.torilab.assignment.notes.di.NotesUIDI

class Injector private constructor(
    applicationContext: Context,
) {
    private val noteComponentDI = NoteComponentDI(applicationContext)
    val addNoteUIDI = AddNoteUIDI(noteComponentDI.addNote)
    val notesUIDI = NotesUIDI(noteComponentDI.getNotes, noteComponentDI.getNoteDetail)
    val noteDetailUIDI =
        NoteDetailUIDI(noteComponentDI.getObservableNoteDetail, noteComponentDI.updateNote, noteComponentDI.deleteNote)
    val noteEventStateHolder = notesUIDI.noteEventStateHolder

    companion object {
        lateinit var INSTANCE: Injector
            private set

        fun start(applicationContext: Context) {
            INSTANCE = Injector(applicationContext)
        }
    }
}

val injector = Injector.INSTANCE
