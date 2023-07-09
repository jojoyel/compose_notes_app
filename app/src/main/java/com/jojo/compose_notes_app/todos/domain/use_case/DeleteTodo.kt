package com.jojo.compose_notes_app.todos.domain.use_case

import com.jojo.compose_notes_app.todos.domain.model.Todo
import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import javax.inject.Inject

class DeleteTodo @Inject constructor(private val repository: TodosRepository) {
    suspend operator fun invoke(todo: Todo) {
        todo.id?.let { repository.deleteTodo(it) }
    }
}
