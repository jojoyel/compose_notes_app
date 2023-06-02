package com.jojo.compose_notes_app.todos.domain.use_case

import com.jojo.compose_notes_app.todos.domain.model.Todo
import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import javax.inject.Inject

class InsertTodo @Inject constructor(private val repository: TodosRepository) {
    suspend operator fun invoke(todo: Todo) {
        repository.insertTodo(todo)
    }
}
