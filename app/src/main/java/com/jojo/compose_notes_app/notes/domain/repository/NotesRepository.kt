package com.jojo.compose_notes_app.util.notes.domain.repository

import com.jojo.compose_notes_app.util.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun insertNote(note: Note)

    fun deleteNote(id: Int)

    fun getNote(id: Int): Note

    fun getNotes(): Flow<List<Note>>
}