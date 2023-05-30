package com.jojo.compose_notes_app.notes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jojo.compose_notes_app.notes.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("""DELETE FROM notes WHERE id = :id""")
    suspend fun deleteNote(id: Int)

    @Query("""SELECT * FROM notes WHERE id = :id""")
    suspend fun getNote(id: Int): NoteEntity

    @Query("""SELECT * FROM notes""")
    fun getNotes(): Flow<List<NoteEntity>>

}
