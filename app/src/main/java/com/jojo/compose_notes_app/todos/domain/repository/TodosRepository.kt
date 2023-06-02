package com.jojo.compose_notes_app.todos.domain.repository

import com.jojo.compose_notes_app.todos.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodosRepository {
    suspend fun getTodo(id: Int): Todo

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(id: Int)

    suspend fun getTodos(): Flow<List<Todo>>
}