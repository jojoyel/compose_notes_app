package com.jojo.compose_notes_app.notes.presentation

import com.jojo.compose_notes_app.notes.domain.model.Note

data class NotesListState(
    val notes: List<Note> = emptyList(),
    val hasNotes: Boolean = false,
    val displayNotes: List<Note> = emptyList()
)
