package com.jojo.compose_notes_app.notes.domain.use_case

data class NotesUseCases(
    val getNotes: GetNotes,
    val getSingleNote: GetSingleNote,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote
)
