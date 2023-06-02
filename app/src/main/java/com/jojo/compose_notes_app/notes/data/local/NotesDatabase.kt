package com.jojo.compose_notes_app.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jojo.compose_notes_app.notes.data.local.entity.NoteEntity
import com.jojo.compose_notes_app.todos.data.local.TodosDao
import com.jojo.compose_notes_app.todos.data.local.entity.TodoEntity

@Database(version = 1, entities = [NoteEntity::class, TodoEntity::class])
abstract class NotesDatabase : RoomDatabase() {

    abstract val notesDao: NotesDao
    abstract val todosDao: TodosDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}