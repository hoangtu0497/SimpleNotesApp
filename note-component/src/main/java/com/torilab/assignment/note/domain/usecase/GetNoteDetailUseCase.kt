package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.repository.NoteRepository

internal class GetNoteDetailUseCase(private val repository: NoteRepository) : GetNoteDetail {
    override suspend fun invoke(id: Int): Answer<Note, Unit> {
        return repository.getNoteById(id)
    }
}
