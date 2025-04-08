package com.torilab.assignment.notes.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.torilab.assignment.note.domain.usecase.GetNoteDetail
import com.torilab.assignment.note.domain.usecase.GetNotes
import com.torilab.assignment.notes.presentation.navigationdata.NoteEventState
import com.torilab.assignment.notes.presentation.navigationdata.NoteEventStateHolder
import com.torilab.assignment.notes.presentation.navigationdata.RealNoteEventStateHolder
import com.torilab.assignment.notes.presentation.view.NotesScreen
import com.torilab.assignment.notes.presentation.viewmodel.NotesScreenState
import com.torilab.assignment.notes.presentation.viewmodel.NotesViewModel
import com.torilab.assignment.notes.presentation.viewmodel.RealNotesViewModel
import com.torilab.assignment.viewmodel.StateDelegate

class NotesUIDI(
    private val getNotes: GetNotes,
    private val getNoteDetail: GetNoteDetail,
) {

    @Composable
    private fun makeNotesViewModel(): NotesViewModel {
        return viewModel {
            RealNotesViewModel(
                getNotes,
                getNoteDetail,
                StateDelegate(NotesScreenState()),
            )
        }
    }

    @Composable
    fun NotesScreenDI(
        noteEvent: NoteEventState?,
        navigateToAddScreen: () -> Unit = {},
        navigateToDetailScreen: (Int) -> Unit = {},
    ) {
        NotesScreen(
            makeNotesViewModel(),
            noteEvent,
            navigateToAddScreen,
            navigateToDetailScreen,
        )
    }

    val noteEventStateHolder: NoteEventStateHolder by lazy { RealNoteEventStateHolder() }
}
