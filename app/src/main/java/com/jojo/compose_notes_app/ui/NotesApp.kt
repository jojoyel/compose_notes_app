package com.jojo.compose_notes_app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jojo.compose_notes_app.R
import com.jojo.compose_notes_app.ui.theme.NotesAppTheme
import com.jojo.compose_notes_app.util.NavBarItems
import com.jojo.compose_notes_app.util.Routes
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

@Composable
fun NotesApp(windowSizeClass: WindowWidthSizeClass) {
    NotesAppTheme {
        val context = LocalContext.current

        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        var fabClickedFlow by remember { mutableStateOf(emptyFlow<Unit>()) }

        when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> {
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

                                FloatingActionButtonType.ADD_TODO ->
                                    fabClickedFlow = flow { emit(Unit) }
                            }
                        }
                    }
                ) { padding ->
                    MainNav(navController, snackbarHostState, fabClickedFlow, padding)
                }
            }

            else -> {
                Row(Modifier.fillMaxSize()) {
                    NavigationRail {
                        Spacer(modifier = Modifier.weight(1f))
                        NavBarItems.values().forEach { item ->
                            val selected =
                                currentDestination?.hierarchy?.any { it.route == item.route } == true
                            NavigationRailItem(
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
                                icon = {
                                    Icon(
                                        if (selected) item.selectedIcon else item.defaultIcon,
                                        contentDescription = null
                                    )
                                },
                                label = { Text(stringResource(item.displayName)) }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Scaffold(
                        modifier = Modifier.weight(1f),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        floatingActionButton = {
                            ActionButton(navController = navController, extended = true) {
                                when (it) {
                                    FloatingActionButtonType.ADD_NOTE ->
                                        navController.navigate("${Routes.Note.route}/-1")

                                    FloatingActionButtonType.ADD_TODO ->
                                        fabClickedFlow = flow { emit(Unit) }
                                }
                            }
                        }
                    ) { padding ->
                        MainNav(navController, snackbarHostState, fabClickedFlow, padding)
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
    extended: Boolean = false,
    onClick: (FloatingActionButtonType) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (currentDestination?.route) {
        Routes.NotesList.route -> {
            if (extended)
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.action_add_note)) },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_add_note)
                        )
                    },
                    onClick = { onClick(FloatingActionButtonType.ADD_NOTE) })
            else
                FloatingActionButton(onClick = { onClick(FloatingActionButtonType.ADD_NOTE) }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.action_add_note)
                    )
                }
        }

        Routes.TodosList.route -> {
            if (extended)
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.action_add_todo)) },
                    icon = {
                        Icon(
                            Icons.Default.AddTask,
                            contentDescription = stringResource(R.string.action_add_todo)
                        )
                    },
                    onClick = { onClick(FloatingActionButtonType.ADD_TODO) })
            else
                FloatingActionButton(onClick = { onClick(FloatingActionButtonType.ADD_TODO) }) {
                    Icon(
                        Icons.Default.AddTask,
                        contentDescription = stringResource(R.string.action_add_todo)
                    )
                }
        }

        else -> {}
    }
}

private enum class FloatingActionButtonType {
    ADD_NOTE, ADD_TODO
}