package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.repository.NoteRepository

internal class GetNotesUseCase(private val repository: NoteRepository) : GetNotes {
    override suspend fun invoke(pageSize: Int, offset: Int): Answer<List<Note>, Unit> {
        return repository.getNoteList(pageSize, offset)
    }
}
