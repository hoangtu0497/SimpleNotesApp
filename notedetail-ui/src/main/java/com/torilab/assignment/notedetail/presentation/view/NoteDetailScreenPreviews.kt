package com.torilab.assignment.notedetail.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailDisplayState
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailScreenEvent
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailScreenState
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailViewModel
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

// These should be screenshotTests if I have time.
@Preview
@Composable
private fun PreviewNoteDetailScreenInitialLoadingState() {
    NoteDetailScreen(createViewModelWithState(NoteDetailScreenState()))
}

@Preview
@Composable
private fun PreviewNoteDetailScreenViewingDetailState() {
    NoteDetailScreen(
        createViewModelWithState(
            NoteDetailScreenState(
                titleText = dummyNote.title,
                descriptionText = dummyNote.note,
                note = dummyNote,
                displayState = NoteDetailDisplayState.Detail,
            )
        )
    )
}

@Preview
@Composable
private fun PreviewNoteDetailScreenEditingNoteState() {
    NoteDetailScreen(
        createViewModelWithState(
            NoteDetailScreenState(
                titleText = "Entering new title",
                descriptionText = "Entering new description",
                note = dummyNote,
                displayState = NoteDetailDisplayState.Editing,
            )
        )
    )
}

private fun createViewModelWithState(noteDetailScreenState: NoteDetailScreenState): NoteDetailViewModel {
    return TestNoteDetailViewModel(MutableStateFlow(noteDetailScreenState), emptyFlow())
}

private class TestNoteDetailViewModel(
    flowState: StateFlow<NoteDetailScreenState>,
    flowViewEvent: Flow<NoteDetailScreenEvent>,
) : NoteDetailViewModel,
    StateViewModel<NoteDetailScreenState>,
    EventViewModel<NoteDetailScreenEvent> {

    override val state = flowState
    override val viewEvent = flowViewEvent

    override fun setEditing(isEditing: Boolean) {}
    override fun onTitleChanged(text: String) {}
    override fun onDescriptionChanged(text: String) {}
    override fun updateNote() {}
    override fun deleteNote() {}
}

private val dummyNote = Note(title = "This is title", note = "This is description")
