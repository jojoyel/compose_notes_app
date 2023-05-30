package com.jojo.compose_notes_app.notes.domain.use_case

import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(private val repository: NotesRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}