package com.torilab.assignment.note.domain.model

sealed interface UpdateNoteError {
    data object EmptyTitle : UpdateNoteError
    data object EmptyDescription : UpdateNoteError
    data object GeneralError : UpdateNoteError
}
