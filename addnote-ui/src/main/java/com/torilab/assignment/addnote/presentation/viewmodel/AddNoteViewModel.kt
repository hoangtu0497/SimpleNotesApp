package com.torilab.assignment.addnote.presentation.viewmodel

import androidx.annotation.StringRes
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateViewModel

internal interface AddNoteViewModel : StateViewModel<AddNoteScreenState>, EventViewModel<AddNoteScreenEvent> {
    fun onTitleChanged(text: String)
    fun onDescriptionChanged(text: String)

    fun addNewNote()
}

internal data class AddNoteScreenState(
    val titleText: String = "",
    val descriptionText: String = "",
    val displayState: AddNoteDisplayState = AddNoteDisplayState.Input,
)

internal sealed interface AddNoteDisplayState {
    data object Loading : AddNoteDisplayState
    data object Input : AddNoteDisplayState
}

internal sealed interface AddNoteScreenEvent {
    data class SuccessfullyAdded(val newNoteId: Int) : AddNoteScreenEvent
    data class Error(@StringRes val errorStringRes: Int) : AddNoteScreenEvent
}
