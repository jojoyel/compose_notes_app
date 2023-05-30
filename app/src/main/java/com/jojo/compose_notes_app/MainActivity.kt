package com.jojo.compose_notes_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jojo.compose_notes_app.notes.presentation.NotesListScreen
import com.jojo.compose_notes_app.notes.presentation.NotesListViewModel
import com.jojo.compose_notes_app.notes.presentation.details.NoteDetailsScreen
import com.jojo.compose_notes_app.notes.presentation.details.NoteDetailsViewModel
import com.jojo.compose_notes_app.ui.theme.NotesAppTheme
import com.jojo.compose_notes_app.util.NavBarItems
import com.jojo.compose_notes_app.util.Routes
import com.jojo.compose_notes_app.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesAppTheme {
                val context = LocalContext.current

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        BottomBar(navController = navController)
                    },
                    floatingActionButton = {
                        ActionButton(navController = navController) {
                            when (it) {
                                FloatingActionButtonType.ADD_NOTE ->
                                    navController.navigate("${Routes.Note.route}/-1")
                            }
                        }
                    }) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.NotesList.route,
                        modifier = Modifier.padding(padding)
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
                                                    it.message.asString(
                                                        context
                                                    )
                                                )
                                            }
                                        }

                                        else -> {}
                                    }
                                }
                            }

                            NoteDetailsScreen(
                                state = noteDetailsViewModel.state,
                                onEvent = { noteDetailsViewModel.onEvent(it) }
                            )
                        }
                        composable(Routes.TodosList.route) {
                            Text("A venir")
                        }
                        composable(Routes.Settings.route) {
                            Text("A venir")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomBar(navController: NavController) {
        val items = NavBarItems.values()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val routesShowingNavBar = listOf("${Routes.Note.route}/{id}")

        AnimatedVisibility(
            visible = !routesShowingNavBar.contains(currentDestination?.route),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            NavigationBar {
                items.forEach { item ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                if (selected) item.selectedIcon else item.defaultIcon,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(item.displayName)) })
                }
            }
        }
    }

    @Composable
    private fun ActionButton(
        navController: NavController,
        onClick: (FloatingActionButtonType) -> Unit
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        when (currentDestination?.route) {
            Routes.NotesList.route -> {
                FloatingActionButton(onClick = { onClick(FloatingActionButtonType.ADD_NOTE) }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.action_add_note)
                    )
                }
            }

            else -> {}
        }
    }

    private enum class FloatingActionButtonType {
        ADD_NOTE
    }
}