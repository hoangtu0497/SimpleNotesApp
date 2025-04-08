package com.torilab.assignment.notedetail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.torilab.assignment.foundations.onSuccess
import com.torilab.assignment.foundations.util.launchWithExceptionHandler
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.model.UpdateNoteError
import com.torilab.assignment.note.domain.usecase.DeleteNote
import com.torilab.assignment.note.domain.usecase.GetObservableNoteDetail
import com.torilab.assignment.note.domain.usecase.UpdateNote
import com.torilab.assignment.viewmodel.EventDelegate
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateDelegate
import com.torilab.assignment.viewmodel.StateViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

internal class RealNoteDetailViewModel(
    private val noteId: Int,
    private val getObservableNoteDetail: GetObservableNoteDetail,
    private val updateNote: UpdateNote,
    private val deleteNote: DeleteNote,
    private val stateDelegate: StateDelegate<NoteDetailScreenState>,
    private val eventDelegate: EventDelegate<NoteDetailScreenEvent>,
) : StateViewModel<NoteDetailScreenState> by stateDelegate,
    EventViewModel<NoteDetailScreenEvent> by eventDelegate,
    NoteDetailViewModel,
    ViewModel() {

    private val currentScreenState: NoteDetailScreenState
        get() = state.value

    private val currentNoteModel: Note?
        get() = currentScreenState.note

    init {
        viewModelScope.launchWithExceptionHandler {
            getObservableNoteDetail(noteId).onSuccess { noteFlow ->
                noteFlow.distinctUntilChanged().collect { note ->
                    stateDelegate.updateState {
                        it.copy(
                            titleText = note.title,
                            descriptionText = note.note,
                            note = note,
                            displayState = NoteDetailDisplayState.Detail,
                        )
                    }
                }
            }
        }
    }

    override fun setEditing(isEditing: Boolean) {
        if (isEditing) {
            updateDisplayState(NoteDetailDisplayState.Editing)
        } else {
            stateDelegate.updateState {
                it.copy(
                    titleText = currentNoteModel?.title.orEmpty(),
                    descriptionText = currentNoteModel?.note.orEmpty(),
                    displayState = NoteDetailDisplayState.Detail,
                )
            }
        }
    }

    override fun onTitleChanged(text: String) {
        stateDelegate.updateState { it.copy(titleText = text) }
    }

    override fun onDescriptionChanged(text: String) {
        stateDelegate.updateState { it.copy(descriptionText = text) }
    }

    override fun updateNote() {
        viewModelScope.launchWithExceptionHandler(onException = { setEditing(true) }) {
            val updatedNoteModel = currentNoteModel?.copy(
                title = currentScreenState.titleText,
                note = currentScreenState.descriptionText,
            ) ?: return@launchWithExceptionHandler

            updateDisplayState(NoteDetailDisplayState.Loading)
            updateNote(updatedNoteModel).foldSuspend(
                success = {
                    eventDelegate.sendEvent(NoteDetailScreenEvent.UpdateNoteSuccessfully(noteId))
                    setEditing(false)
                },
                error = {
                    eventDelegate.sendEvent(NoteDetailScreenEvent.Error(mapErrorToErrorStringRes(it)))
                    setEditing(true)
                },
            )

        }
    }

    override fun deleteNote() {
        viewModelScope.launchWithExceptionHandler(onException = { updateDisplayState(NoteDetailDisplayState.Detail) }) {
            val noteId = currentNoteModel?.id ?: return@launchWithExceptionHandler

            updateDisplayState(NoteDetailDisplayState.Loading)
            deleteNote(noteId).onSuccess { eventDelegate.sendEvent(NoteDetailScreenEvent.DeleteNoteSuccessfully(noteId)) }
        }
    }

    private fun updateDisplayState(newState: NoteDetailDisplayState) {
        stateDelegate.updateState { it.copy(displayState = newState) }
    }

    private fun mapErrorToErrorStringRes(error: UpdateNoteError) = when (error) {
        UpdateNoteError.EmptyTitle -> com.torilab.assignment.designsystem.R.string.error_title_cannot_be_empty
        UpdateNoteError.EmptyDescription -> com.torilab.assignment.designsystem.R.string.error_description_cannot_be_empty
        UpdateNoteError.GeneralError -> com.torilab.assignment.designsystem.R.string.error_general
    }
}
