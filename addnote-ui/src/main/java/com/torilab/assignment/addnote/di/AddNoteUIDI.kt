package com.torilab.assignment.addnote.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.torilab.assignment.addnote.presentation.view.AddNoteScreen
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteScreenState
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteViewModel
import com.torilab.assignment.addnote.presentation.viewmodel.RealAddNoteViewModel
import com.torilab.assignment.note.domain.usecase.AddNote
import com.torilab.assignment.viewmodel.EventDelegate
import com.torilab.assignment.viewmodel.StateDelegate

class AddNoteUIDI(
    private val addNote: AddNote,
) {

    @Composable
    private fun makeAddNoteViewModel(): AddNoteViewModel {
        return viewModel {
            RealAddNoteViewModel(
                addNote,
                StateDelegate(AddNoteScreenState()),
                EventDelegate(),
            )
        }
    }

    @Composable
    fun AddNoteScreenDI(onBack: () -> Unit, onNoteAdded: (Int) -> Unit) {
        AddNoteScreen(
            makeAddNoteViewModel(),
            onBack,
            onNoteAdded,
        )
    }
}
