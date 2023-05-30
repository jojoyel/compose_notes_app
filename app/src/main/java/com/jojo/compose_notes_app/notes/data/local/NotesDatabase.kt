package com.jojo.compose_notes_app.util.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jojo.compose_notes_app.util.notes.data.local.entity.NoteEntity

@Database(version = 1, entities = [NoteEntity::class])
abstract class NotesDatabase : RoomDatabase() {

    abstract val dao: NotesDao


    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}