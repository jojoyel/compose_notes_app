package com.jojo.compose_notes_app.util

sealed class Routes(val route: String) {
    object NotesList : Routes("notes")
    object Note : Routes("note")
    object TodosList : Routes("todos")
    object Settings : Routes("settings")
}
