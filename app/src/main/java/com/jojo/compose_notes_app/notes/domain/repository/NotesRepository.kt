package com.jojo.compose_notes_app.notes.domain.repository

import com.jojo.compose_notes_app.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(id: Int)

    suspend fun getNote(id: Int): Note

    fun getNotes(): Flow<List<Note>>
}