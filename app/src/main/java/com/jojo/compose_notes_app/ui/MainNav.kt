package com.jojo.compose_notes_app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jojo.compose_notes_app.notes.presentation.NotesListScreen
import com.jojo.compose_notes_app.notes.presentation.NotesListViewModel
import com.jojo.compose_notes_app.notes.presentation.details.NoteDetailsScreen
import com.jojo.compose_notes_app.notes.presentation.details.NoteDetailsViewModel
import com.jojo.compose_notes_app.todos.presentation.TodosListEvent
import com.jojo.compose_notes_app.todos.presentation.TodosListScreen
import com.jojo.compose_notes_app.todos.presentation.TodosListViewModel
import com.jojo.compose_notes_app.util.Routes
import com.jojo.compose_notes_app.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun MainNav(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    fabClickedFlow: Flow<Unit>,
    padding: PaddingValues
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.NotesList.route,
        modifier = androidx.compose.ui.Modifier.padding(padding)
    ) {
        composable(Routes.NotesList.route) {
            val notesViewModel = hiltViewModel<NotesListViewModel>()

            NotesListScreen(state = notesViewModel.state) {
                navController.navigate("${Routes.Note.route}/$it")
            }
        }
        composable(
            "${Routes.Note.route}/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            val noteDetailsViewModel = hiltViewModel<NoteDetailsViewModel>()

            LaunchedEffect(true) {
                noteDetailsViewModel.uiEvent.collect {
                    when (it) {
                        UiEvent.GoBack -> navController.navigateUp()
                        is UiEvent.GoBackWithMessage -> {
                            navController.navigateUp()
                            CoroutineScope(Dispatchers.IO).launch {
                                snackbarHostState.showSnackbar(
                                    it.message.asString(context)
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }

            NoteDetailsScreen(
                state = noteDetailsViewModel.state,
                onEvent = noteDetailsViewModel::onEvent
            )
        }
        composable(Routes.TodosList.route) {
            val todosListViewModel = hiltViewModel<TodosListViewModel>()

            LaunchedEffect(fabClickedFlow) {
                fabClickedFlow.collect {
                    todosListViewModel.onEvent(TodosListEvent.OnOpenDialog())
                }
            }

            TodosListScreen(
                state = todosListViewModel.state,
                event = todosListViewModel::onEvent
            )
        }
        composable(Routes.Settings.route) {
            Text("A venir")
        }
    }
}