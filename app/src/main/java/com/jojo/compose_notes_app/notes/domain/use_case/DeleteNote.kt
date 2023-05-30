package com.jojo.compose_notes_app.notes.domain.use_case

import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNote @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteNote(id)
    }
}