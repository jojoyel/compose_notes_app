package com.jojo.compose_notes_app.util.notes.data.repository

import com.jojo.compose_notes_app.util.notes.data.local.NotesDao
import com.jojo.compose_notes_app.util.notes.data.mapper.toNote
import com.jojo.compose_notes_app.util.notes.domain.model.Note
import com.jojo.compose_notes_app.util.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(private val dao: NotesDao): NotesRepository {
    override fun insertNote(note: Note) {
        dao.insertNote(note.to)
    }

    override fun deleteNote(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getNote(id: Int): Note {
        TODO("Not yet implemented")
    }

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { entity -> entity.map { it.toNote() } }
    }
}