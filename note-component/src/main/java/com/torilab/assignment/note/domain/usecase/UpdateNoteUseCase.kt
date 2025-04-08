package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.model.UpdateNoteError
import com.torilab.assignment.note.domain.repository.NoteRepository

internal class UpdateNoteUseCase(private val repository: NoteRepository) : UpdateNote {
    override suspend fun invoke(note: Note): Answer<Unit, UpdateNoteError> {
        return when {
            !note.isTitleValid -> Answer.Error(UpdateNoteError.EmptyTitle)
            !note.isNoteValid -> Answer.Error(UpdateNoteError.EmptyDescription)
            else -> repository.updateNote(note)
        }
    }
}
