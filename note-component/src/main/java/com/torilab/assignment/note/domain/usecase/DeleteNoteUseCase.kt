package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.repository.NoteRepository

internal class DeleteNoteUseCase(private val repository: NoteRepository) : DeleteNote {
    override suspend fun invoke(id: Int): Answer<Unit, Unit> {
        return repository.deleteNote(id)
    }
}
