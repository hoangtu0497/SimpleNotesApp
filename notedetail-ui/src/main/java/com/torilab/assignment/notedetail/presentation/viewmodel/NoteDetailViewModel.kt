package com.torilab.assignment.notedetail.presentation.viewmodel

import androidx.annotation.StringRes
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateViewModel

internal interface NoteDetailViewModel : StateViewModel<NoteDetailScreenState>, EventViewModel<NoteDetailScreenEvent> {
    fun setEditing(isEditing: Boolean)
    fun onTitleChanged(text: String)
    fun onDescriptionChanged(text: String)

    fun updateNote()
    fun deleteNote()
}

internal data class NoteDetailScreenState(
    val titleText: String = "",
    val descriptionText: String = "",
    val note: Note? = null,
    val displayState: NoteDetailDisplayState = NoteDetailDisplayState.Loading,
)

internal sealed interface NoteDetailDisplayState {
    data object Loading : NoteDetailDisplayState
    data object Detail : NoteDetailDisplayState
    data object Editing : NoteDetailDisplayState
}

internal sealed interface NoteDetailScreenEvent {
    data class UpdateNoteSuccessfully(val id: Int) : NoteDetailScreenEvent
    data class DeleteNoteSuccessfully(val id: Int) : NoteDetailScreenEvent
    data class Error(@StringRes val errorStringRes: Int) : NoteDetailScreenEvent
}
