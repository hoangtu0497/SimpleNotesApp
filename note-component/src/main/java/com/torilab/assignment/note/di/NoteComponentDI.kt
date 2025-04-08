package com.torilab.assignment.note.di

import android.content.Context
import com.torilab.assignment.note.data.db.NoteDatabase
import com.torilab.assignment.note.data.repository.RealNoteRepository
import com.torilab.assignment.note.domain.repository.NoteRepository
import com.torilab.assignment.note.domain.usecase.AddNote
import com.torilab.assignment.note.domain.usecase.AddNoteUseCase
import com.torilab.assignment.note.domain.usecase.DeleteNote
import com.torilab.assignment.note.domain.usecase.DeleteNoteUseCase
import com.torilab.assignment.note.domain.usecase.GetNoteDetail
import com.torilab.assignment.note.domain.usecase.GetNoteDetailUseCase
import com.torilab.assignment.note.domain.usecase.GetNotes
import com.torilab.assignment.note.domain.usecase.GetNotesUseCase
import com.torilab.assignment.note.domain.usecase.GetObservableNoteDetail
import com.torilab.assignment.note.domain.usecase.GetObservableNoteDetailUseCase
import com.torilab.assignment.note.domain.usecase.UpdateNote
import com.torilab.assignment.note.domain.usecase.UpdateNoteUseCase

class NoteComponentDI(context: Context) {

    private val dao by lazy {
        NoteDatabase.getInstance(context).dao()
    }

    private val repository: NoteRepository by lazy {
        RealNoteRepository(dao)
    }

    val addNote: AddNote by lazy {
        AddNoteUseCase(repository)
    }

    val getNotes: GetNotes by lazy {
        GetNotesUseCase(repository)
    }

    val getNoteDetail: GetNoteDetail by lazy {
        GetNoteDetailUseCase(repository)
    }

    val getObservableNoteDetail: GetObservableNoteDetail by lazy {
        GetObservableNoteDetailUseCase(repository)
    }

    val updateNote: UpdateNote by lazy {
        UpdateNoteUseCase(repository)
    }

    val deleteNote: DeleteNote by lazy {
        DeleteNoteUseCase(repository)
    }
}
