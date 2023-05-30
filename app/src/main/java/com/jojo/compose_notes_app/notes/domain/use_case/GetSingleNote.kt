package com.jojo.compose_notes_app.notes.domain.use_case

import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository


class GetSingleNote(private val repository: NotesRepository) {
    suspend operator fun invoke(id: Int): Note = repository.getNote(id)
}
