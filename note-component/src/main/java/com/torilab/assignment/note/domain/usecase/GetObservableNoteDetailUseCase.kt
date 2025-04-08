package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

internal class GetObservableNoteDetailUseCase(private val repository: NoteRepository) : GetObservableNoteDetail {
    override fun invoke(id: Int): Answer<Flow<Note>, Unit> {
        return repository.getNoteFlowById(id)
    }
}
