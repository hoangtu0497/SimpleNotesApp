package com.torilab.assignment.notes.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.notes.presentation.viewmodel.NotesScreenState
import com.torilab.assignment.notes.presentation.viewmodel.NotesViewModel
import com.torilab.assignment.viewmodel.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// These should be screenshotTests if I have time.
@Preview
@Composable
private fun PreviewNotesScreenInitialState() {
    NotesScreen(createViewModelWithState(NotesScreenState()))
}

@Preview
@Composable
private fun PreviewNotesScreenInitialLoadingState() {
    NotesScreen(createViewModelWithState(NotesScreenState(isLoading = true)))
}

@Preview
@Composable
private fun PreviewNotesScreenWithItemsState() {
    NotesScreen(
        createViewModelWithState(
            NotesScreenState(
                List(100) { Note(title = "Title $it", note = "Description $it") }
            )
        )
    )
}

@Preview
@Composable
private fun PreviewNotesScreenLoadingMoreState() {
    NotesScreen(
        createViewModelWithState(
            NotesScreenState(
                notes = List(100) { Note(title = "Title $it", note = "Description $it") },
                isLoading = true,
            )
        )
    )
}

private fun createViewModelWithState(notesScreenState: NotesScreenState): NotesViewModel {
    return TestNotesViewModel(MutableStateFlow(notesScreenState))
}

private class TestNotesViewModel(
    flowState: StateFlow<NotesScreenState>,
) : NotesViewModel,
    StateViewModel<NotesScreenState> {

    override val state = flowState

    override fun initialLoadNotes() {}
    override fun loadMoreNotes() {}
    override fun onNoteAdded(id: Int) {}
    override fun onNoteUpdated(id: Int) {}
    override fun onNoteDeleted(id: Int) {}
}
