package com.torilab.assignment.notedetail.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.torilab.assignment.note.domain.usecase.DeleteNote
import com.torilab.assignment.note.domain.usecase.GetObservableNoteDetail
import com.torilab.assignment.note.domain.usecase.UpdateNote
import com.torilab.assignment.notedetail.presentation.view.NoteDetailScreen
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailScreenState
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailViewModel
import com.torilab.assignment.notedetail.presentation.viewmodel.RealNoteDetailViewModel
import com.torilab.assignment.viewmodel.EventDelegate
import com.torilab.assignment.viewmodel.StateDelegate

class NoteDetailUIDI(
    private val getObservableNoteDetail: GetObservableNoteDetail,
    private val updateNote: UpdateNote,
    private val deleteNote: DeleteNote,
) {

    @Composable
    private fun makeNoteDetailViewModel(noteId: Int): NoteDetailViewModel {
        return viewModel {
            RealNoteDetailViewModel(
                noteId,
                getObservableNoteDetail,
                updateNote,
                deleteNote,
                StateDelegate(NoteDetailScreenState()),
                EventDelegate(),
            )
        }
    }

    @Composable
    fun NoteDetailScreenDI(
        noteId: Int,
        onBack: () -> Unit = {},
        onNoteDeleted: (Int) -> Unit,
        onNoteUpdated: (Int) -> Unit,
    ) {
        NoteDetailScreen(
            makeNoteDetailViewModel(noteId),
            onBack,
            onNoteDeleted,
            onNoteUpdated,
        )
    }
}
