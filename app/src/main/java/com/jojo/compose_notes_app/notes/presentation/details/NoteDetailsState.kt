package com.jojo.compose_notes_app.notes.presentation.details

import com.jojo.compose_notes_app.notes.domain.model.Note

data class NoteDetailsState(
    val originalNote: Note? = null,
    val note: Note = Note(null, "", "", null, false),
    val loading: Boolean = true
)
