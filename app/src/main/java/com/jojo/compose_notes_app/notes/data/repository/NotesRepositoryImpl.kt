package com.jojo.compose_notes_app.notes.data.repository

import com.jojo.compose_notes_app.notes.data.local.NotesDao
import com.jojo.compose_notes_app.notes.data.mapper.toNote
import com.jojo.compose_notes_app.notes.data.mapper.toNoteEntity
import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(private val dao: NotesDao): NotesRepository {
    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(id: Int) {
        dao.deleteNote(id)
    }

    override suspend fun getNote(id: Int): Note {
        return dao.getNote(id).toNote()
    }

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { entity -> entity.map { it.toNote() } }
    }
}