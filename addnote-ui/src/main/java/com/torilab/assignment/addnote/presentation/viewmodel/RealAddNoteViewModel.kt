package com.torilab.assignment.addnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.torilab.assignment.foundations.util.launchWithExceptionHandler
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.usecase.AddNote
import com.torilab.assignment.viewmodel.EventDelegate
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateDelegate
import com.torilab.assignment.viewmodel.StateViewModel

internal class RealAddNoteViewModel(
    private val addNote: AddNote,
    private val stateDelegate: StateDelegate<AddNoteScreenState>,
    private val eventDelegate: EventDelegate<AddNoteScreenEvent>,
) : StateViewModel<AddNoteScreenState> by stateDelegate,
    EventViewModel<AddNoteScreenEvent> by eventDelegate,
    AddNoteViewModel,
    ViewModel() {

    override fun onTitleChanged(text: String) {
        stateDelegate.updateState { it.copy(titleText = text) }
    }

    override fun onDescriptionChanged(text: String) {
        stateDelegate.updateState { it.copy(descriptionText = text) }
    }

    override fun addNewNote() {
        viewModelScope.launchWithExceptionHandler(onException = { updateDisplayState(AddNoteDisplayState.Input) }) {
            updateDisplayState(AddNoteDisplayState.Loading)
            val note = with(state.value) { Note(title = titleText, note = descriptionText) }
            addNote(note).foldSuspend(
                success = { eventDelegate.sendEvent(AddNoteScreenEvent.SuccessfullyAdded(it.toInt())) },
                error = { eventDelegate.sendEvent(AddNoteScreenEvent.Error(mapErrorToErrorStringRes(it))) },
            )
            updateDisplayState(AddNoteDisplayState.Input)
        }
    }

    private fun updateDisplayState(newState: AddNoteDisplayState) {
        stateDelegate.updateState { it.copy(displayState = newState) }
    }

    private fun mapErrorToErrorStringRes(error: AddNoteError) = when (error) {
        AddNoteError.EmptyTitle -> com.torilab.assignment.designsystem.R.string.error_title_cannot_be_empty
        AddNoteError.EmptyDescription -> com.torilab.assignment.designsystem.R.string.error_description_cannot_be_empty
        AddNoteError.GeneralError -> com.torilab.assignment.designsystem.R.string.error_general
    }
}
