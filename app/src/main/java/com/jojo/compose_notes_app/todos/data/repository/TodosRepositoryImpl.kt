package com.jojo.compose_notes_app.todos.data.repository

import com.jojo.compose_notes_app.todos.data.local.TodosDao
import com.jojo.compose_notes_app.todos.data.mapper.toTodo
import com.jojo.compose_notes_app.todos.data.mapper.toTodoEntity
import com.jojo.compose_notes_app.todos.domain.model.Todo
import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodosRepositoryImpl(private val dao: TodosDao) : TodosRepository {
    override suspend fun getTodo(id: Int): Todo {
        return dao.getTodo(id).toTodo()
    }

    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo.toTodoEntity())
    }

    override suspend fun deleteTodo(id: Int) {
        dao.deleteTodo(id)
    }

    override suspend fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos().map { entity -> entity.map { it.toTodo() } }
    }
}