package com.jojo.compose_notes_app.todos.domain.use_case

import com.jojo.compose_notes_app.todos.domain.model.Todo
import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodos @Inject constructor(private val repository: TodosRepository) {

    suspend operator fun invoke(): Flow<List<Todo>> =
        repository.getTodos()
}
