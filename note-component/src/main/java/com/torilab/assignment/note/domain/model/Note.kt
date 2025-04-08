package com.torilab.assignment.note.domain.model

import java.util.Date

data class Note(
    val id: Int = 0,
    val title: String,
    val note: String,
    val createdAt: Date = Date(System.currentTimeMillis()),
    val updatedAt: Date = Date(System.currentTimeMillis()),
) {
    val isTitleValid: Boolean
        get() = title.isNotBlank()

    val isNoteValid: Boolean
        get() = note.isNotBlank()
}
