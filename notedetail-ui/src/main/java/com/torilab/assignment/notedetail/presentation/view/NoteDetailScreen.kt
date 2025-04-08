package com.torilab.assignment.notedetail.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.torilab.assignment.designsystem.DescriptionTextField
import com.torilab.assignment.designsystem.LoadingDialog
import com.torilab.assignment.designsystem.MyAppBar
import com.torilab.assignment.designsystem.NoteTitleTextField
import com.torilab.assignment.designsystem.TopNavBarIcon
import com.torilab.assignment.foundations.util.rememberDebouncedClick
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailDisplayState
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailScreenEvent
import com.torilab.assignment.notedetail.presentation.viewmodel.NoteDetailViewModel
import com.torilab.assignment.notedetail.ui.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteDetailScreen(
    noteDetailViewModel: NoteDetailViewModel,
    onBack: () -> Unit = {},
    onNoteDeleted: (Int) -> Unit = {},
    onNoteUpdated: (Int) -> Unit = {},
) {
    val appBarState = rememberTopAppBarState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val rememberedScrollBehavior = remember { scrollBehavior }

    val screenState by noteDetailViewModel.state.collectAsState()
    val displayState = screenState.displayState
    val isEditingState = displayState is NoteDetailDisplayState.Editing

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        var snackbarJob: Job? = null

        fun showSnackbar(message: String) {
            snackbarJob?.cancel()
            snackbarJob = launch {
                snackbarHostState.showSnackbar(message)
            }
        }

        noteDetailViewModel.viewEvent.collect { viewEvent ->
            when (viewEvent) {
                is NoteDetailScreenEvent.UpdateNoteSuccessfully -> {
                    onNoteUpdated(viewEvent.id)
                    showSnackbar(context.getString(R.string.note_updated))
                }

                is NoteDetailScreenEvent.DeleteNoteSuccessfully -> {
                    onNoteDeleted(viewEvent.id)
                    showSnackbar(context.getString(R.string.note_deleted))
                }

                is NoteDetailScreenEvent.Error -> showSnackbar(context.getString(viewEvent.errorStringRes))
            }
        }
    }

    Scaffold(
        topBar = {
            MyAppBar(
                text = if (isEditingState) "" else stringResource(id = R.string.note_detail),
                scrollBehavior = rememberedScrollBehavior,
                navigationIcon = {
                    if (isEditingState) {
                        TopNavBarIcon(
                            image = Icons.Filled.Close,
                            description = stringResource(com.torilab.assignment.designsystem.R.string.back_nav_icon),
                            onClick = rememberDebouncedClick { noteDetailViewModel.setEditing(false) },
                        )
                    } else {
                        TopNavBarIcon(
                            image = Icons.AutoMirrored.Filled.ArrowBack,
                            description = stringResource(com.torilab.assignment.designsystem.R.string.back_nav_icon),
                            onClick = rememberDebouncedClick { onBack() }
                        )
                    }
                },
                actions = {
                    TopNavBarIcon(
                        image = Icons.Filled.Delete,
                        description = stringResource(R.string.delete_icon),
                        onClick = rememberDebouncedClick { noteDetailViewModel.deleteNote() },
                    )
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = rememberDebouncedClick {
                    if (isEditingState) {
                        noteDetailViewModel.updateNote()
                    } else {
                        noteDetailViewModel.setEditing(true)
                    }
                },
                text = {
                    Text(
                        if (isEditingState) stringResource(com.torilab.assignment.designsystem.R.string.save)
                        else stringResource(R.string.edit),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isEditingState) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = stringResource(com.torilab.assignment.designsystem.R.string.fab),
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    NoteTitleTextField(
                        text = screenState.titleText,
                        onValueChange = { noteDetailViewModel.onTitleChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = !isEditingState,
                        placeholder = {
                            if (isEditingState) {
                                Text(
                                    text = stringResource(com.torilab.assignment.designsystem.R.string.title_textField_input),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                    )
                    DescriptionTextField(
                        text = screenState.descriptionText,
                        onValueChange = { noteDetailViewModel.onDescriptionChanged(it) },
                        modifier = Modifier.fillMaxSize(),
                        readOnly = !isEditingState,
                        placeholder = {
                            if (isEditingState) {
                                Text(
                                    text = stringResource(com.torilab.assignment.designsystem.R.string.body_textField_input),
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                    )
                }
            }
        },
    )

    LoadingDialog(isOpened = displayState is NoteDetailDisplayState.Loading)
}
