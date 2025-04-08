package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.model.UpdateNoteError
import kotlinx.coroutines.flow.Flow

fun interface AddNote {
    suspend operator fun invoke(note: Note): Answer<Long, AddNoteError>
}

fun interface GetNotes {
    suspend operator fun invoke(pageSize: Int, offset: Int): Answer<List<Note>, Unit>
}

fun interface GetNoteDetail {
    suspend operator fun invoke(id: Int): Answer<Note, Unit>
}

fun interface GetObservableNoteDetail {
    operator fun invoke(id: Int): Answer<Flow<Note>, Unit>
}

fun interface UpdateNote {
    suspend operator fun invoke(note: Note): Answer<Unit, UpdateNoteError>
}

fun interface DeleteNote {
    suspend operator fun invoke(id: Int): Answer<Unit, Unit>
}
