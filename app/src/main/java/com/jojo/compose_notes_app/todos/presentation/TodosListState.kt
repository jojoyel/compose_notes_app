package com.jojo.compose_notes_app.todos.presentation

import com.jojo.compose_notes_app.todos.domain.model.Todo

data class TodosListState(
    val todos: List<Todo> = emptyList(),
    val editionDialogVisible: Boolean = false,
    val editionDialogData: Todo? = null
)
