package com.torilab.assignment.mynoteapp.app.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.torilab.assignment.mynoteapp.app.di.injector
import com.torilab.assignment.mynoteapp.app.navigation.Route.Detail.ARG_NOTE_ID
import com.torilab.assignment.notes.presentation.navigationdata.NoteEventStateHolder

@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val noteEventStateHolder = remember { injector.noteEventStateHolder }

    Surface(
        modifier = Modifier.safeDrawingPadding(),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.List.routeName,
            builder = {
                listScreen(navController, noteEventStateHolder)
                addScreen(navController, noteEventStateHolder)
                detailScreen(navController, noteEventStateHolder)
            },
        )
    }
}

private fun NavGraphBuilder.listScreen(
    navController: NavController,
    noteEventStateHolder: NoteEventStateHolder,
) {
    composable(Route.List.routeName) {
        injector.notesUIDI.NotesScreenDI(
            noteEvent = noteEventStateHolder.event,
            navigateToAddScreen = { navController.navigate(Route.Add.routeName) },
            navigateToDetailScreen = { noteId ->
                navController.navigate("${Route.Detail.routeName}/$noteId")
            },
        )
    }
}

private fun NavGraphBuilder.addScreen(
    navController: NavController,
    noteEventStateHolder: NoteEventStateHolder,
) {
    composable(Route.Add.routeName) {
        injector.addNoteUIDI.AddNoteScreenDI(
            onBack = { navController.popBackStack() },
            onNoteAdded = { newNoteId ->
                noteEventStateHolder.notifyNoteAdded(newNoteId)
                navController.popBackStack()
            },
        )
    }
}

private fun NavGraphBuilder.detailScreen(
    navController: NavController,
    noteEventStateHolder: NoteEventStateHolder,
) {
    composable(
        route = "${Route.Detail.routeName}/{$ARG_NOTE_ID}",
        arguments = listOf(navArgument(ARG_NOTE_ID) { type = NavType.IntType }),
    ) {
        val noteId = it.arguments?.getInt(ARG_NOTE_ID) ?: -1
        injector.noteDetailUIDI.NoteDetailScreenDI(
            noteId = noteId,
            onBack = { navController.popBackStack() },
            onNoteDeleted = { deletedNoteId ->
                noteEventStateHolder.notifyNoteDeleted(deletedNoteId)
                navController.popBackStack()
            },
            onNoteUpdated = { updatedNoteId ->
                noteEventStateHolder.notifyNoteUpdated(updatedNoteId)
                navController.popBackStack()
            },
        )
    }
}

private sealed class Route(val routeName: String) {
    data object List : Route("list")
    data object Add : Route("add")
    data object Detail : Route("detail") {
        const val ARG_NOTE_ID = "noteId"
    }
}
