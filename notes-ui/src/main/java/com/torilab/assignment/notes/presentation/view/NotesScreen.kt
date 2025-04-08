package com.torilab.assignment.notes.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torilab.assignment.designsystem.LoadingDialog
import com.torilab.assignment.designsystem.MyAppBar
import com.torilab.assignment.notes.presentation.navigationdata.NoteAdded
import com.torilab.assignment.notes.presentation.navigationdata.NoteDeleted
import com.torilab.assignment.notes.presentation.navigationdata.NoteEventState
import com.torilab.assignment.notes.presentation.navigationdata.NoteUpdated
import com.torilab.assignment.notes.presentation.viewmodel.NotesViewModel
import com.torilab.assignment.notes.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotesScreen(
    notesViewModel: NotesViewModel,
    noteEvent: NoteEventState? = null,
    navigateToAddScreen: () -> Unit = {},
    navigateToDetailScreen: (Int) -> Unit = {},
) {
    val screenState by notesViewModel.state.collectAsState()
    val notes = screenState.notes
    LaunchedEffect(Unit) {
        notesViewModel.initialLoadNotes()
    }

    val listState = rememberLazyListState()
    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastDisplayedIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            return@derivedStateOf lastDisplayedIndex >= totalItems - PRELOAD_THRESHOLD_ITEMS
        }
    }
    LaunchedEffect(fetchNextPage) {
        if (fetchNextPage) notesViewModel.loadMoreNotes()
    }

    LaunchedEffect(noteEvent) {
        when (noteEvent) {
            is NoteAdded -> notesViewModel.onNoteAdded(noteEvent.noteId)
            is NoteDeleted -> notesViewModel.onNoteDeleted(noteEvent.noteId)
            is NoteUpdated -> notesViewModel.onNoteUpdated(noteEvent.noteId)
            else -> Unit
        }
    }

    Scaffold(
        topBar = { MyAppBar(stringResource(id = R.string.app_name)) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToAddScreen() },
                text = {
                    Text(
                        stringResource(R.string.add),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(com.torilab.assignment.designsystem.R.string.fab),
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 96.dp),
                    state = listState,
                ) {
                    items(items = notes) {
                        Box(Modifier.padding(horizontal = 16.dp)) {
                            NoteItem(
                                data = it,
                                onClick = { navigateToDetailScreen(it.id) },
                            )
                        }
                    }
                }
            }
        }
    )

    LoadingDialog(isOpened = screenState.isLoading)
}

private const val PRELOAD_THRESHOLD_ITEMS = 3
