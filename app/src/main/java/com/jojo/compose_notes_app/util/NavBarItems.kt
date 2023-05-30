package com.jojo.compose_notes_app.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.jojo.compose_notes_app.R

enum class NavBarItems(
    val route: String,
    @StringRes val displayName: Int,
    val selectedIcon: ImageVector,
    val defaultIcon: ImageVector
) {
    NOTES(
        Routes.NotesList.route,
        R.string.nav_notes,
        Icons.Filled.Description,
        Icons.Outlined.Description
    ),
    TODOS(
        Routes.TodosList.route,
        R.string.nav_todos,
        Icons.Filled.Checklist,
        Icons.Default.Checklist
    ),
    SETTINGS(
        Routes.Settings.route,
        R.string.nav_settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    )
}