package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.repository.NoteRepository

internal class AddNoteUseCase(private val repository: NoteRepository) : AddNote {
    override suspend fun invoke(note: Note): Answer<Long, AddNoteError> {
        return when {
            !note.isTitleValid -> Answer.Error(AddNoteError.EmptyTitle)
            !note.isNoteValid -> Answer.Error(AddNoteError.EmptyDescription)
            else -> repository.insertNote(note)
        }
    }
}
