package com.jojo.compose_notes_app.todos.data.mapper

import com.jojo.compose_notes_app.todos.data.local.entity.TodoEntity
import com.jojo.compose_notes_app.todos.domain.model.Todo

fun Todo.toTodoEntity(): TodoEntity =
    TodoEntity(
        id = this.id,
        title = this.title,
        urgent = this.urgent,
        completed = this.completed,
        creationDate = this.creationDate ?: "",
        completedDate = this.completedDate ?: ""
    )

fun TodoEntity.toTodo(): Todo =
    Todo(
        id = this.id,
        title = this.title,
        urgent = this.urgent,
        completed = this.completed,
        creationDate = this.creationDate,
        completedDate = this.completedDate
    )