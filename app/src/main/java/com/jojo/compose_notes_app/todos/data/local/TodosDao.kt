package com.jojo.compose_notes_app.todos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jojo.compose_notes_app.todos.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Query("""DELETE FROM todos WHERE id = :id""")
    suspend fun deleteTodo(id: Int)

    @Query("""SELECT * FROM todos WHERE id = :id""")
    suspend fun getTodo(id: Int): TodoEntity

    @Query("""SELECT * FROM todos""")
    fun getTodos(): Flow<List<TodoEntity>>
}
