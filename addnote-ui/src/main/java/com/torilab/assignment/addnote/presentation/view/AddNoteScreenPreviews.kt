package com.torilab.assignment.addnote.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteDisplayState
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteScreenEvent
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteScreenState
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteViewModel
import com.torilab.assignment.viewmodel.EventViewModel
import com.torilab.assignment.viewmodel.StateViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

// These should be screenshotTests if I have time.
@Preview
@Composable
private fun PreviewAddNoteScreenInitialState() {
    AddNoteScreen(createViewModelWithState(AddNoteScreenState()))
}

@Preview
@Composable
private fun PreviewAddNoteScreenLoadingState() {
    AddNoteScreen(
        createViewModelWithState(
            AddNoteScreenState(
                titleText = "Note Title",
                descriptionText = "Note Description",
                displayState = AddNoteDisplayState.Loading,
            )
        )
    )
}

private fun createViewModelWithState(addNoteScreenState: AddNoteScreenState): AddNoteViewModel {
    return TestAddNoteViewModel(MutableStateFlow(addNoteScreenState), emptyFlow())
}

private class TestAddNoteViewModel(
    flowState: StateFlow<AddNoteScreenState>,
    flowViewEvent: Flow<AddNoteScreenEvent>,
) : AddNoteViewModel,
    StateViewModel<AddNoteScreenState>,
    EventViewModel<AddNoteScreenEvent> {

    override val state = flowState
    override val viewEvent = flowViewEvent

    override fun onTitleChanged(text: String) {}
    override fun onDescriptionChanged(text: String) {}
    override fun addNewNote() {}
}
