package com.torilab.assignment.note.domain.model

sealed interface AddNoteError {
    data object EmptyTitle : AddNoteError
    data object EmptyDescription : AddNoteError
    data object GeneralError : AddNoteError
}
