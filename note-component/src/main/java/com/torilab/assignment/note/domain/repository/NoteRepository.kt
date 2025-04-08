package com.torilab.assignment.note.domain.repository

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.model.UpdateNoteError
import kotlinx.coroutines.flow.Flow

internal interface NoteRepository {
    suspend fun getNoteList(pageSize: Int, offset: Int): Answer<List<Note>, Unit>
    suspend fun insertNote(note: Note): Answer<Long, AddNoteError>
    suspend fun getNoteById(id: Int): Answer<Note, Unit>
    fun getNoteFlowById(id: Int): Answer<Flow<Note>, Unit>
    suspend fun updateNote(note: Note): Answer<Unit, UpdateNoteError>
    suspend fun deleteNote(id: Int): Answer<Unit, Unit>
}
