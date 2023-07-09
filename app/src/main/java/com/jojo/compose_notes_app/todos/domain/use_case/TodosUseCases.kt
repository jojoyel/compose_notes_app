package com.jojo.compose_notes_app.todos.domain.use_case

data class TodosUseCases(
    val getTodos: GetTodos,
    val insertTodo: InsertTodo,
    val deleteTodo: DeleteTodo
)
