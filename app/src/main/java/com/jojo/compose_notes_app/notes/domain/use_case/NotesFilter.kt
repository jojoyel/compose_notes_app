package com.jojo.compose_notes_app.notes.domain.use_case

sealed class NotesFilter {
    object Descending : NotesFilter()
    object Ascending : NotesFilter()
}
