package com.jojo.compose_notes_app.notes.presentation.details

sealed class NoteDetailsEvent {
    data class OnTitleChanged(val title: String) : NoteDetailsEvent()
    data class OnContentChanged(val content: String) : NoteDetailsEvent()
    data class OnColorChanged(val color: Long?) : NoteDetailsEvent()
    object OnFavoriteToggled : NoteDetailsEvent()
    object OnCreateNoteClicked : NoteDetailsEvent()
    object OnDeleteClicked : NoteDetailsEvent()
}
