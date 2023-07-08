package com.jojo.compose_notes_app.notes.presentation

import com.jojo.compose_notes_app.util.SelectableColors

sealed class NotesListEvent {
    data class OnColorFilterChanged(val color: SelectableColors?) : NotesListEvent()
}
