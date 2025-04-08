package com.torilab.assignment.addnote.presentation.view

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
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteDisplayState
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteScreenEvent
import com.torilab.assignment.addnote.presentation.viewmodel.AddNoteViewModel
import com.torilab.assignment.addnote.ui.R
import com.torilab.assignment.designsystem.DescriptionTextField
import com.torilab.assignment.designsystem.LoadingDialog
import com.torilab.assignment.designsystem.MyAppBar
import com.torilab.assignment.designsystem.NoteTitleTextField
import com.torilab.assignment.designsystem.TopNavBarIcon
import com.torilab.assignment.foundations.util.rememberDebouncedClick
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNoteScreen(
    addNoteViewModel: AddNoteViewModel,
    onBack: () -> Unit = {},
    onNoteAdded: (Int) -> Unit = {},
) {
    val appBarState = rememberTopAppBarState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val rememberedScrollBehavior = remember { scrollBehavior }
    val screenState by addNoteViewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        var snackbarJob: Job? = null
        addNoteViewModel.viewEvent.collect { viewEvent ->
            when (viewEvent) {
                is AddNoteScreenEvent.SuccessfullyAdded -> onNoteAdded(viewEvent.newNoteId)
                is AddNoteScreenEvent.Error -> {
                    snackbarJob?.cancel()
                    snackbarJob = launch { snackbarHostState.showSnackbar(context.getString(viewEvent.errorStringRes)) }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MyAppBar(
                text = stringResource(R.string.add_new_note),
                scrollBehavior = rememberedScrollBehavior,
                navigationIcon = {
                    TopNavBarIcon(
                        image = Icons.AutoMirrored.Filled.ArrowBack,
                        description = stringResource(com.torilab.assignment.designsystem.R.string.back_nav_icon),
                        onClick = rememberDebouncedClick { onBack() },
                    )
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = rememberDebouncedClick { addNoteViewModel.addNewNote() },
                text = {
                    Text(
                        stringResource(com.torilab.assignment.designsystem.R.string.save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Check,
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
                        onValueChange = { addNoteViewModel.onTitleChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = stringResource(com.torilab.assignment.designsystem.R.string.title_textField_input),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    )

                    DescriptionTextField(
                        text = screenState.descriptionText,
                        onValueChange = { addNoteViewModel.onDescriptionChanged(it) },
                        modifier = Modifier.fillMaxSize(),
                        placeholder = {
                            Text(
                                text = stringResource(com.torilab.assignment.designsystem.R.string.body_textField_input),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    )
                }
            }
        },
    )

    LoadingDialog(isOpened = screenState.displayState is AddNoteDisplayState.Loading)
}
